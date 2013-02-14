/*
 * Gephi Seadragon Plugin
 *
 * Copyright 2010-2011 Gephi
 * Authors : Mathieu Bastian <mathieu.bastian@gephi.org>
 * Website : http://www.gephi.org
 * Licensed under Apache 2 License (http://www.apache.org/licenses/LICENSE-2.0)
 */
package org.gephi.plugins.seadragon;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.gephi.io.exporter.preview.PNGExporter;
import org.gephi.io.exporter.spi.Exporter;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.project.api.Workspace;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.Progress;
import org.gephi.utils.progress.ProgressTicket;
import org.openide.util.Lookup;
import org.w3c.dom.Element;
import processing.core.PGraphicsJava2D;
import processing.core.PImage;

/**
 *
 * @author Mathieu Bastian
 */
public class SeadragonExporter implements Exporter, LongTask {

    //Const
    private static final String XML_FILE = "map.xml";
    private static final String PATH_MAP = "map";
    private static final String PATH_FILES = "_files";
    //Architecture
    private Workspace workspace;
    private ProgressTicket progress;
    private boolean cancel = false;
    private PNGExporter pngExporter = new PNGExporter();
    private TileRenderer tileRenderer;
    //Settings
    private int width;
    private int height;
    private int margin;
    private File path;
    private int overlap = 1;
    private int tileSize = 256;
    private boolean bUseWalker = true;
    private int viewWidth;
    private int viewHeight;
    
    @Override
    public boolean execute() {
        if(this.bUseWalker) { 
            return execute_walker();
        } else {
            return execute_old();
        }
    }

    private boolean execute_old() {
        Progress.start(progress);
        Progress.setDisplayName(progress, "Export Seadragon");
        
        PreviewController controller = Lookup.getDefault().lookup(PreviewController.class);
        controller.getModel(workspace).getProperties().putValue(PreviewProperty.VISIBILITY_RATIO, 1.0);
        controller.refreshPreview(workspace);
        
        PreviewProperties props = controller.getModel(workspace).getProperties();
        props.putValue("width", width);
        props.putValue("height", height);
        props.putValue(PreviewProperty.MARGIN, new Float((float) margin));
        ProcessingTarget target = (ProcessingTarget) controller.getRenderTarget(RenderTarget.PROCESSING_TARGET, workspace);
        
        target.refresh();
        
        // allocate whole requested buffer at once
        Progress.switchToIndeterminate(progress);
        PGraphicsJava2D pg2 = (PGraphicsJava2D) target.getGraphics();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        img.setRGB(0, 0, width, height, pg2.pixels, 0, width);
        
        try {
            export(img);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
        createXML();
        exportOtherFiles();
        
        Progress.finish(progress);
        
        return !cancel;
    }

    // allocate only small bitmaps a time
    private boolean execute_walker() {

        Progress.start(progress);
        Progress.setDisplayName(progress, "Export Seadragon (Walker)");
        
        PreviewController controller = Lookup.getDefault().lookup(PreviewController.class);
        controller.getModel(workspace).getProperties().putValue(PreviewProperty.VISIBILITY_RATIO, 1.0);
        controller.refreshPreview(workspace);

        // create directory that will contain tiles
        File tiles = new File(path, PATH_MAP + File.separator + PATH_MAP + PATH_FILES);
        tiles.mkdirs();

        // number of zoom levels is log2(max dimension)
        int numLevels = (int) Math.ceil(Math.log(Math.max(width, height)) / Math.log(2.));
        
        // count all tiles we're about to export
        int tasks = 0;
        for (int level = numLevels; level >= 0; level--) {
            // scale = 1 / (2^x)
            float levelScale = 1f / (1 << (numLevels - level));
            tasks += (int) Math.ceil(levelScale * width / tileSize) * (int) Math.ceil(levelScale * height / tileSize);
        }

        Progress.switchToDeterminate(progress, tasks);

        // for each level generate a canvas and save it to tiles
        for (int level = numLevels; level >= 0 && !cancel; level--) {
            // calculate the scale
            float levelScale = 1f / (1 << (numLevels - level));
            int levelw = (int)(width * levelScale);
            int levelh = (int)(height * levelScale);
            
            // create destination folder
            File levelFolder = new File(tiles.getAbsolutePath() + File.separator + level);
            levelFolder.mkdirs();
            
            // create headless canvas according to
            // http://gephi.org/docs/api/org/gephi/preview/api/ProcessingTarget.html
            PreviewProperties props = controller.getModel(workspace).getProperties();
            props.putValue("width", levelw);
            props.putValue("height", levelh);
            props.putValue(PreviewProperty.MARGIN, new Float((float) margin));
            ProcessingTarget target = (ProcessingTarget) controller.getRenderTarget(RenderTarget.PROCESSING_TARGET, workspace);
            target.refresh();

            PGraphicsJava2D pg2 = (PGraphicsJava2D) target.getGraphics();

            // for each tile export the pixels to png
            int cols = (int) Math.ceil(levelw / tileSize);
            int rows = (int) Math.ceil(levelh / tileSize);
            for (int r = 0; r <= rows; r++) {
                for (int c = 0; c <= cols; c++) {
                    if (tileSize * c < levelw && tileSize * r < levelh) {
                        int left = c != 0 ? tileSize * c - overlap : 0;
                        int top = r != 0 ? tileSize * r - overlap : 0;
                        int tileW = (int) (tileSize + (c == 0 ? 1 : 2) * (double) overlap);
                        int tileH = (int) (tileSize + (r == 0 ? 1 : 2) * (double) overlap);
                        tileW = Math.min(tileW, levelw - left);
                        tileH = Math.min(tileH, levelh - top);

                        // get the pixel data
                        PImage tiledImage = pg2.get(left, top, tileW, tileH);
                        BufferedImage tiledImageBI = new BufferedImage(tileW, tileH, BufferedImage.TYPE_INT_ARGB);
                        
                        tiledImage.loadPixels();
                        tiledImageBI.setRGB(0, 0, tileW, tileH, tiledImage.pixels, 0, tileW);
                        
                        File fout = new File(levelFolder, c + "_" + r + ".png");
                        try {
                            ImageIO.write(tiledImageBI, "png", fout);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
/*                        
                        //PImage tiledImage = new PImage(tileW, tileH);
                        //pg2.copy(tiledImage, left, top, tileW, tileH, 0, 0, tileW, tileH);
                        
                        // save the pixels
                        String outFile = levelFolder.getAbsolutePath() + File.separator + c + "_" + r + ".png";
                        try {
                            tiledImage.save(outFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
*/
                        if (cancel) break;
                        
                        Progress.progress(progress);
                    }
                }
            }
        }
       
        createXML();
        exportOtherFiles();
        
        Progress.finish(progress);
        return !cancel;
    }
    
    public void export(BufferedImage img) throws Exception {
        delete(new File(path, PATH_MAP));
        File folder = new File(path, PATH_MAP);
        folder.mkdirs();
        folder = new File(folder, PATH_MAP + PATH_FILES);
        folder.mkdir();
        
        int numLevels = (int) Math.ceil(Math.log(Math.max(img.getWidth(), img.getHeight())) / Math.log(2.));
        int w = img.getWidth();
        int h = img.getHeight();
        
        // Calculate tasks count
        int tasks = 0;
        for (int level = numLevels; level >= 0; level--) {
            float levelScale = 1f / (1 << (numLevels - level));
            tasks += (int) Math.ceil(levelScale * w / tileSize) * (int) Math.ceil(levelScale * h / tileSize);
        }

        Progress.switchToDeterminate(progress, tasks);
        
        //Tile renderer
        tileRenderer = new TileRenderer(folder, tileSize, overlap);
        tileRenderer.setProgressTicket(progress);
        for (int level = numLevels; level >= 0 && !cancel; level--) {
            File levelFolder = new File(folder, "" + (level));
            levelFolder.mkdir();
            float levelScale = 1f / (1 << (numLevels - level));
            tileRenderer.writeLevel(img, levelScale, level);
        }
        
        tileRenderer = null;
    }
    
    public void createXML() {
        File file = new File(path + File.separator + PATH_MAP + File.separator + XML_FILE);
        org.w3c.dom.Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            document = documentBuilder.newDocument();
            document.setXmlVersion("1.0");
            document.setXmlStandalone(true);
        } catch (Exception ex) {
            throw new RuntimeException("Can't create XML file", ex);
        }
        
        Element imageElement = document.createElement("Image");
        imageElement.setAttribute("TileSize", String.valueOf(tileSize));
        imageElement.setAttribute("Overlap", String.valueOf(overlap));
        imageElement.setAttribute("Format", "png");
        imageElement.setAttribute("ServerFormat", "Default");
        imageElement.setAttribute("xmlns", "http://schemas.microsoft.com/deepzoom/2009");
        
        Element sizeElement = document.createElement("Size");
        sizeElement.setAttribute("Width", String.valueOf(width));
        sizeElement.setAttribute("Height", String.valueOf(height));
        imageElement.appendChild(sizeElement);
        document.appendChild(imageElement);
        
        try {
            Source source = new DOMSource(document);
            Result result = new StreamResult(file);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, result);
        } catch (Exception ex) {
            throw new RuntimeException("Can't write XML file", ex);
        }
    }
    
    private void exportOtherFiles() {
        try {
            HashMap hm = new HashMap();
            hm.put("canvas_width", String.valueOf(viewWidth));
            hm.put("canvas_height", String.valueOf(viewHeight));
            copyFromJarTemplated("seadragon.html", path, hm);
            copyFromJar("img/fullpage_grouphover.png", path);
            copyFromJar("img/fullpage_hover.png", path);
            copyFromJar("img/fullpage_pressed.png", path);
            copyFromJar("img/fullpage_rest.png", path);
            copyFromJar("img/home_grouphover.png", path);
            copyFromJar("img/home_hover.png", path);
            copyFromJar("img/home_pressed.png", path);
            copyFromJar("img/home_rest.png", path);
            copyFromJar("img/zoomin_grouphover.png", path);
            copyFromJar("img/zoomin_hover.png", path);
            copyFromJar("img/zoomin_pressed.png", path);
            copyFromJar("img/zoomin_rest.png", path);
            copyFromJar("img/zoomout_grouphover.png", path);
            copyFromJar("img/zoomout_hover.png", path);
            copyFromJar("img/zoomout_pressed.png", path);
            copyFromJar("img/zoomout_rest.png", path);
            copyFromJar("js/seadragon-min.js", path);
        } catch (Exception ex) {
            Logger.getLogger(SeadragonExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    
    private void writeStrToFile(File file, String str) {
        try {
            PrintWriter out = new PrintWriter(file);
            out.print(str);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void copyFromJarTemplated(String source, File folder, Map kval) {
        Set entries = kval.entrySet();
        Iterator it = entries.iterator();
        
        // load the data
        InputStream is = getClass().getResourceAsStream("/org/gephi/plugins/seadragon/resources/" + source);
        String page = convertStreamToString(is);

        while(it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            String key = (String) e.getKey();
            String val = (String) e.getValue();
            
            page = page.replaceAll("\\{" + key + "\\}", val);
        }
        
        writeStrToFile(new File(folder, source), page);
    }
    
    private void copyFromJar(String source, File folder) throws Exception {
        InputStream is = getClass().getResourceAsStream("/org/gephi/plugins/seadragon/resources/" + source);
        File file = new File(folder + (folder.getPath().endsWith(File.separator) ? "" : File.separator) + source);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        OutputStream os = new FileOutputStream(file);
        byte[] buffer = new byte[4096];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        os.close();
        is.close();
    }
    
    public int getTileSize() {
        return tileSize;
    }
    
    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }
    
    @Override
    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }
    
    @Override
    public Workspace getWorkspace() {
        return workspace;
    }
    
    public int getMargin() {
        return margin;
    }
    
    public void setMargin(int margin) {
        this.margin = margin;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public int getOverlap() {
        return overlap;
    }
    
    public void setOverlap(int overlap) {
        this.overlap = overlap;
    }
    
    public int getWidth() {
        return width;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public File getPath() {
        return path;
    }
    
    public void setPath(File path) {
        this.path = path;
    }
    
    public void setWalker(boolean bUse) {
        this.bUseWalker = bUse;
    }
    
    @Override
    public boolean cancel() {
        this.cancel = true;
        pngExporter.cancel();
        if (tileRenderer != null) {
            tileRenderer.cancel();
        }
        return true;
    }
    
    @Override
    public void setProgressTicket(ProgressTicket progressTicket) {
        this.progress = progressTicket;
    }
    
    private void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                delete(c);
            }
        }
        if (f.exists() && !f.delete()) {
            throw new IOException("Failed to delete file: " + f);
        }
    }
    
    public void setView(int h, int w) {
        this.viewHeight = h;
        this.viewWidth = w;
    }
    
    public int getViewWidth() {
        return viewWidth;
    }
    
    public int getViewHeight() {
        return viewHeight;
    }
}
