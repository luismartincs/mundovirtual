/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package creator;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author luis_
 */
public class NodeCreator {

    /**
     * @param args the command line arguments
     */
    private static void createDir(String destinationDir) {
        Path path = Paths.get(destinationDir);
        //if directory exists?
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                //fail to create directory
                e.printStackTrace();
            }
        }
    }

    public static void createPackages(Hashtable<String, ArrayList<String>> model, Hashtable<String, String> modelNames, String packageName, String destinationDir) throws Exception {

        String dirs[] = packageName.split("\\.");
        destinationDir += "\\";

        for (String dir : dirs) {
            destinationDir += dir + "\\";
        }

        createDir(destinationDir);

        createDir(destinationDir + "config");
        createDir(destinationDir + "model");
        createDir(destinationDir + "main");

        //MainFile
        PrintWriter mainFile = new PrintWriter(destinationDir + "main" + "\\Main.java", "UTF-8");
        mainFile.println("package " + packageName + ".main;");
        mainFile.println("import cFramework.nodes.service.Igniter;");
        mainFile.println();
        mainFile.println("public class Main extends Igniter{");
        mainFile.println();
        mainFile.println("\tpublic Main(){");
        mainFile.println("\t\tString[] areaNames = {");

        //ConfigFile
        PrintWriter configFile = new PrintWriter(destinationDir + "config" + "\\AreaNames.java", "UTF-8");

        configFile.println("package " + packageName + ".config;");
        configFile.println();
        configFile.println("import cFramework.util.IDHelper;");
        configFile.println();
        configFile.println("public class AreaNames {");
        configFile.println();

        //Big and SmallNodes
        for (String key : model.keySet()) {

            String bigNodeName = modelNames.get(key);

            createDir(destinationDir + "model\\" + bigNodeName.toLowerCase());

            System.out.println("BigNode: " + bigNodeName);

            //MainFile
            mainFile.println("\t\t\t" + packageName + ".model." + bigNodeName.toLowerCase() + "." + bigNodeName + ".class.getName(),");

            //Config
            configFile.println();
            configFile.println("\tpublic static long " + bigNodeName + " = IDHelper.generateID(\"" + bigNodeName + "\");");

            //BigNodeClass
            String packageDir = destinationDir + "model\\" + bigNodeName.toLowerCase();

            PrintWriter bigNodeClass = null;

            if (!new File(packageDir + "\\" + bigNodeName + ".java").exists()) {

                bigNodeClass = new PrintWriter(packageDir + "\\" + bigNodeName + ".java", "UTF-8");
                bigNodeClass.println("package " + packageName + ".model." + bigNodeName.toLowerCase() + ";");
                bigNodeClass.println();
                bigNodeClass.println("import " + packageName + ".config.AreaNames;");
                bigNodeClass.println();
                bigNodeClass.println("public class " + bigNodeName + " extends cFramework.nodes.area.Area {");
                bigNodeClass.println();
                bigNodeClass.println("\tpublic " + bigNodeName + " () {");
                bigNodeClass.println("\t\tthis.ID = AreaNames." + bigNodeName + ";");
                bigNodeClass.println("\t\tthis.namer = AreaNames.class;");

            } else {
                System.out.println("BigNode existe");
            }

            int count = 0;
            for (String sNode : model.get(key)) {
                count++;

                System.out.println("\t SmallNode: " + sNode);

                String className = bigNodeName.toUpperCase() + "Process" + count;

                //Config
                configFile.println("\tpublic static long " + className + " = IDHelper.generateID(\"" + bigNodeName + "\",\"" + className + "\");");

                //
                PrintWriter writer = null;

                if (!new File(packageDir + "\\" + className + ".java").exists()) {

                    writer = new PrintWriter(packageDir + "\\" + className + ".java", "UTF-8");

                    writer.println("package " + packageName + ".model." + bigNodeName.toLowerCase() + ";");
                    writer.println();
                    writer.println("import " + packageName + ".config.AreaNames;");
                    writer.println();
                    writer.println("public class " + className + " extends cFramework.nodes.process.Process {");
                    writer.println();
                    writer.println("\tpublic " + className + " () {");
                    writer.println("\t\tthis.ID = AreaNames." + className + ";");
                    writer.println("\t\tthis.namer = AreaNames.class;");
                    writer.println("\t}");
                    writer.println();
                    writer.println("\t@Override");
                    writer.println("\tpublic void receive(long nodeID, byte[] data) {");
                    writer.println("\t}");
                    writer.println("}");

                    writer.close();
                } else {
                    System.out.println("SmallNode existe");
                }

                if (bigNodeClass != null) {
                    bigNodeClass.println("\t\taddProcess(" + className + ".class);");
                }
            }

            //BigNodeClass
            if (bigNodeClass != null) {
                bigNodeClass.println("\t}");
                bigNodeClass.println();
                bigNodeClass.println("\t@Override");
                bigNodeClass.println("\tpublic void init() {");
                bigNodeClass.println("\t}");
                bigNodeClass.println();
                bigNodeClass.println("\t@Override");
                bigNodeClass.println("\tpublic void receive(long nodeID, byte[] data) {");
                bigNodeClass.println("\t}");
                bigNodeClass.println("}");
                bigNodeClass.close();
            }

        }

        //Endconfig
        configFile.println();
        configFile.println("}");
        configFile.close();

        //MainFile
        mainFile.println("\t\t};");
        mainFile.println("\t\tsetAreas(areaNames);");
        mainFile.println("\t\tconfiguration.setLocal(false);");
        mainFile.println("\t\tconfiguration.setDebug(null);");
        mainFile.println("\t\trun();");
        mainFile.println("\t}");
        mainFile.println();
        mainFile.println("\tpublic static void main(String[] arg){");
        mainFile.println("\t\tnew Main();");
        mainFile.println("\t}");
        mainFile.println("}");
        mainFile.close();
    }

    public static void main(String[] args) {

        try {

            String yEdFile = "nucleus-model.graphml";

            String destinationDir = "D:\\Doctorado\\Implementaciones\\ProyectoEstudiantesMaestria\\ProyectoAgentes\\src";
            String packageName = "agents";

            Hashtable<String, ArrayList<String>> model = new Hashtable<>();
            Hashtable<String, String> modelNames = new Hashtable<>();

            File inputFile = new File(yEdFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("node");

            //Find Big and Small Nodes
            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    Element eShape = (Element) eElement.getElementsByTagName("y:Shape").item(0);

                    String id = eElement.getAttribute("id");

                    if (eShape != null) {
                        String type = eShape.getAttribute("type");

                        if (type.equals("octagon")) {

                            model.put(id, new ArrayList<>());

                            modelNames.put(id, nNode.getTextContent().replace("\n", " ").trim());

                            //System.out.println("BigNode: " + id + " " + nNode.getTextContent().trim() + " " + type);
                        } else if (type.equals("ellipse")) {
                            modelNames.put(id, nNode.getTextContent().replace("\n", " ").trim());

                            // System.out.println("SmallNode: " + id + " " + nNode.getTextContent().trim() + " " + type);
                        }
                    }

                }

            }

            ///
            NodeList edges = doc.getElementsByTagName("edge");

            for (int i = 0; i < edges.getLength(); i++) {

                Node nNode = edges.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String source = eElement.getAttribute("source");
                    String target = eElement.getAttribute("target");

                    if (model.containsKey(source) && model.containsKey(target)) {
                        continue;
                    }

                    ArrayList<String> smallNodes = model.get(source);

                    if (smallNodes != null) {

                        String smallNodeName = modelNames.get(target);

                        if (smallNodeName != null) {
                            smallNodes.add(smallNodeName);
                        }

                    } else {
                        smallNodes = model.get(target);

                        if (smallNodes != null) {
                            String smallNodeName = modelNames.get(source);
                            if (smallNodeName != null) {
                                smallNodes.add(smallNodeName);
                            }
                        }
                    }

                }
            }

            createPackages(model, modelNames, packageName, destinationDir);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
