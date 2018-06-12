
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class Bootstrap {

    private static final String INPUT_DIR = "\\Users\\Akinlosose Damilare\\Documents\\ebmsumcorpus-code-ddcc708b53b82cb4588192995fa274c671982bc6\\Abstracts";
    private static final String OUTPUT_DIR = "./output/";
    private static final String TOKEN_DIR = "./tokens/";
    private static final String ABSTRACT_DIR = "./abstracts/";
    private static final String SENTENCES_DIR = "./sentences/";
    private static final String OPEN_LP_MODEL = "C:\\Users\\Akinlosose Damilare\\Documents\\ebmsumcorpus-code-ddcc708b53b82cb4588192995fa274c671982bc6\\en-sent.bin";
    private static final String FILE_EXT = ".txt";
    private String pmid;

    private int totalLength;

    private int totalSentences = 0;

    public Bootstrap() {
        //check if output directory exists if not then create it

        File outDir = new File(OUTPUT_DIR);

        if (!outDir.exists()) {
            outDir.mkdir();
        }

        File tokensDir = new File(TOKEN_DIR);

        if (!tokensDir.exists()) {
            tokensDir.mkdir();
        }

        File abstractDir = new File(ABSTRACT_DIR);

        if (!abstractDir.exists()) {
            abstractDir.mkdir();
        }

        File sentencesDir = new File(SENTENCES_DIR);

        if (!sentencesDir.exists()) {
            sentencesDir.mkdir();
        }
    }

    private void tokenize(String fileName) {

        try {
            FileInputStream fstream = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;

            //System.out.println("pmid gotten " + pmid);
            String outFilename = TOKEN_DIR + pmid + FILE_EXT;
            FileOutputStream fos = new FileOutputStream(new File(outFilename));

            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                System.out.println(strLine);
                String s = strLine;
                String[] words = s.split("\\s+");

                for (int i = 0; i < words.length; i++) {
                    if (words[i].contains(".") || words[i].contains(",") || words[i].contains(";") || words[i].contains(":") || words[i].contains("?")
                            || words[i].contains("!")) {
                        words[i] = words[i].replace(".", "").replace(",", "").replace(";", "").replace(":", "").replace("?", "").replace("!", "");
                    }

                }

                for (int i = 0; i < words.length; i++) {
                    fos.write(words[i].getBytes());
                    fos.write("\r\n".getBytes());
                }

            }

            //close file output stream
            if (fos != null) {
                fos.close();
            }

            //Close the input stream
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void senetenceAverage(String[] segmented) {
        System.out.println(segmented.length);
        int count = 0;
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method will scan the directory defines in the above input folder If files
     * are found it will parse the file(s) and extract the text content found in
     * the Node "abstracttext". The text content will be segmented and the
     * output will be written to a file using the "PMID" node tag value as the
     * file name.
     */
    public void parse() {
        //Get all list of files directory
        File inputFile = new File(INPUT_DIR);
        File[] filesArrays = inputFile.listFiles();

        String outFilename = ABSTRACT_DIR + FILE_EXT;
        FileOutputStream fos = null;

        String senFilename = SENTENCES_DIR + FILE_EXT;
        FileOutputStream foSen = null;

        try {

            fos = new FileOutputStream(new File(outFilename));
            foSen = new FileOutputStream(new File(senFilename));

        } catch (Exception e) {
            e.printStackTrace();
        }

        int count = 0;
        int countAb = 0;

        //Loop through each files
        for (File file : filesArrays) {
            File xmlFile = new File(file.getAbsolutePath());

            try {

                DocumentBuilderFactory dbFactry = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactry.newDocumentBuilder();
                Document dom = dBuilder.parse(xmlFile);
                dom.normalize();

                NodeList pmidNodeList = dom.getElementsByTagName("pmid");

                NodeList nTitle = dom.getElementsByTagName("title");
                NodeList nArticleTitle = dom.getElementsByTagName("articletitle");
                NodeList abstractNodeList = dom.getElementsByTagName("abstracttext");

                //Get the PMID from the current XML file
                String pmid = pmidNodeList.item(0).getTextContent().trim();
                this.pmid = pmid;
                //System.out.println("pmid given " + pmid);
                try {
                    /*File f = new File("./output/" + pmid + ".txt");
                    if (f.exists() && !f.isDirectory()) {
                        System.out.println(f.getName() + "Already exists");
                    } else {*/
                    String title = nTitle.item(0).getTextContent().trim();
                    String artileTitle = nArticleTitle.item(0).getTextContent().trim();

                    StringBuilder sb = new StringBuilder();

                    if (abstractNodeList.getLength() != 1 && abstractNodeList.getLength() != 0) {
                        countAb++;
                        System.out.println(countAb + " Abstract files which have more than one section read");
                        fos.write(pmid.getBytes());
                        fos.write(" ".getBytes());
                        fos.write(String.valueOf(abstractNodeList.getLength()).getBytes());
                        fos.write(" ".getBytes());
                        fos.write("number of sentence length".getBytes());
                        fos.write("\r\n".getBytes());
                    }

                    for (int j = 0; j < abstractNodeList.getLength(); j++) {

                        Node abstractNode = abstractNodeList.item(j);

                        String label = "";
                        String nlmcategory = "";
                        String abstractText = "";

                        if (abstractNode.getNodeType() == Node.ELEMENT_NODE) {
                            //System.out.println("here:" + abstractNode.getNodeName() + ": " + abstractNode.getTextContent());
                        }

                        //check if current tag node has attributes
                        if (abstractNode.hasAttributes()) {
                            if (abstractNode.getAttributes().getNamedItem("label") != null) {
                                label = abstractNode.getAttributes().getNamedItem("label").getNodeValue();
                            }

                            if (abstractNode.getAttributes().getNamedItem("nlmcategory") != null) {
                                nlmcategory = abstractNode.getAttributes().getNamedItem("nlmcategory").getNodeValue();
                            }

                            //System.out.println("Label = " + label + "\t NLM Category = " + nlmcategory);
                        }

                        //combine each abstract text to paragraph. Note, if the text is trimmed then it may affect the segmented output text
                        abstractText = abstractNode.getTextContent();
                        sb.append(abstractText);
                    }

                    System.out.println("Abstract Text Paragraph for file " + xmlFile);
                    System.out.println("----------------------------------------------------------------- ");
                    //System.out.println(sb.toString() + "\n\n");

                    //call sentence segmentation method
                    String segmented[] = segementSentence(sb.toString());

                    totalSentences += segmented.length;

                    //senetenceAverage(segmented);
                    //write output to file
                    writeToFile(pmid, title, artileTitle, segmented);
                    tokenize("./output/" + pmid + ".txt");
                    //}
                    //System.out.println("pmid gotten " + pmid);
                    foSen.write(pmid.getBytes());
                    foSen.write(" ".getBytes());
                    foSen.write("averages: ".getBytes());
                    int countSen = 0;
                    for (int i = 0; i < segmented.length; i++) {
                        String[] words = segmented[i].split("\\s+");

                        for (int j = 0; j < words.length; j++) {
                            countSen++;
                        }

                    }

                    foSen.write(String.valueOf(countSen / (segmented.length + 1)).getBytes());
                    foSen.write(" ".getBytes());
                    foSen.write("\r\n".getBytes());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            count++;
            System.out.println(count + " files read");

        }//end for loop

        double amount = totalSentences / count;
        System.out.println("The average sentence across all files: " + amount);

        try {
            foSen.write("The average sentence across all files: ".getBytes());
            foSen.write(" ".getBytes());
            foSen.write(String.valueOf(amount).getBytes());
            foSen.write("\r\n".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //close file output stream
        if (fos != null) {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (foSen != null) {
            try {
                foSen.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//end method parse()

    /**
     * This method will detect sentences in the from input object using the
     * openLP library. Sentences will be tokenized will be written to file.
     *
     * @param textContent is the input String object to be segmented
     * @throws IOException
     */
    public String[] segementSentence(String textContent) throws IOException {
        InputStream is = new FileInputStream(OPEN_LP_MODEL);
        SentenceModel sModel = new SentenceModel(is);

        //feed the model to SentenceDetectorME class 
        SentenceDetectorME sDetector = new SentenceDetectorME(sModel);

        //detect sentences in the paragraph
        String sentences[] = sDetector.sentDetect(textContent);

        if (is != null) {
            is.close();
        }

        return sentences;
    }

    /**
     * Write the String content to a file.
     *
     * @param filename is the name of the file to be created on the OS level
     * @param content is the String content that'll be written to a file.
     * @throws IOException
     */
    public void writeToFile(String filename, String title, String artileTitle, String contents[]) throws IOException {
        String outFilename = OUTPUT_DIR + filename + FILE_EXT;
        FileOutputStream fos = new FileOutputStream(new File(outFilename));

        //Write title and artile title to file
        fos.write("Title \r\n".getBytes());
        fos.write(title.getBytes());
        fos.write("\r\n".getBytes());
        fos.write("\r\n".getBytes());

        fos.write("Article Title \r\n".getBytes());
        fos.write(artileTitle.getBytes());
        fos.write("\r\n".getBytes());

        fos.write("Abstract Text \r\n".getBytes());
        for (String content : contents) {
            fos.write(content.getBytes());
            fos.write("\r\n".getBytes());
        }
        //close file output stream
        if (fos != null) {
            fos.close();
        }

    }//end method writeToFile()

    public void writeToken(String filename, String title, String artileTitle, String contents[]) throws IOException {
        String outFilename = OUTPUT_DIR + filename + FILE_EXT;
        FileOutputStream fos = new FileOutputStream(new File(outFilename));

        //Write title and artile title to file
        fos.write("Title \r\n".getBytes());
        fos.write(title.getBytes());
        fos.write("\r\n".getBytes());
        fos.write("\r\n".getBytes());

        fos.write("Article Title \r\n".getBytes());
        fos.write(artileTitle.getBytes());
        fos.write("\r\n".getBytes());

        fos.write("Abstract Text \r\n".getBytes());
        for (String content : contents) {
            fos.write(content.getBytes());
            fos.write("\r\n".getBytes());
        }
        //close file output stream
        if (fos != null) {
            fos.close();
        }

    }//end method writeToFile()

    public static void main(String[] args) {
        Bootstrap bp = new Bootstrap();
        System.out.println("Extraction.....");
        LocalDateTime startTime = LocalDateTime.now();
        bp.parse();
        LocalDateTime endTime = LocalDateTime.now();
        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Process ran for : " + Duration.between(startTime, endTime).toMinutes() + " minutes and " + Duration.between(startTime, endTime).getSeconds() + " seconds");
    }

}
