package client;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import rest.model.*;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * CLient REST.
 */
public class Client {

    // Nom de l'application
    private static final String APP_NAME = "Editeur CV";
    // Dimensions par défaut de l'application
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    // URL REST
    private static final QName QNAME = new QName("", "");
    private static final String URL = "http://projetrest.senecdav.cloudbees.net/resume";

    // REST
    private Service service;
    private JAXBContext jc;

    // Composants
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JScrollPane cvScroll;
    private JScrollPane allCVscroll;
    private JScrollPane formScroll;
    private JPanel formPanel;
    private JButton show;
    private JButton reload;
    private JButton save;
    private JButton addITSkill;
    private JButton addLang;
    private JButton addExperiences;
    private JButton addEducation;
    private JTextField cvId;
    private JTextField cvFirstName;
    private JTextField cvName;
    private JTextField cvObjectives;
    private JTextField cvSkill;
    private JTextArea allCvTextArea;
    private JTextArea cvTextArea;
    private List<JTextField> educationsList;
    private List<JTextField> experiencesList;
    private List<JTextField> langsList;
    private List<JTextField> skillsList;

    public Client() {
        try {
            jc = JAXBContext.newInstance(
                    Cv.class,
                    Education.class,
                    Experience.class,
                    ITSkill.class,
                    Lang.class
            );
        } catch (JAXBException je) {
            JOptionPane.showMessageDialog(this.frame, "Impossible de créer le context JAXB !");
            System.exit(0);
        }
        createView();
        placeComponents();
        //createModel();
        createController();
        getAllCV(); // récupère tous les CV au lancement
    }

    private void createView() {
        // Frame
        frame = new JFrame("Editeur de CV");
        frame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        // Tabs
        tabbedPane = new JTabbedPane();
        // Scrolls
        cvScroll = new JScrollPane();
        cvScroll.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        allCVscroll = new JScrollPane();
        formScroll = new JScrollPane();
        // Text Area
        allCvTextArea = new JTextArea("");
        cvTextArea = new JTextArea("");
        allCvTextArea.setEditable(false);
        cvTextArea.setEditable(false);
        // Buttons
        show = new JButton("Afficher");
        reload = new JButton("Recharger");
        save = new JButton("Ajouter");
        addITSkill = new JButton("+");
        addExperiences = new JButton("+");
        addLang = new JButton("+");
        addEducation = new JButton("+");
        // textField
        cvId = new JTextField(4);
        cvFirstName = new JTextField(20);
        cvName = new JTextField(20);
        cvObjectives = new JTextField(20);
        cvSkill = new JTextField(20);
        educationsList = new ArrayList<JTextField>();
        educationsList.add(new JTextField(20));
        skillsList = new ArrayList<JTextField>();
        skillsList.add(new JTextField(20));
        langsList = new ArrayList<JTextField>();
        langsList.add(new JTextField(20));
        experiencesList = new ArrayList<JTextField>();
        experiencesList.add(new JTextField(20));
    }

    private void placeComponents() {
        JPanel p;

        p = new JPanel(new BorderLayout()); {
            JPanel q = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
                q.add(new JLabel("Liste de tous les CV :"));
                q.add(reload);
            }
            p.add(q, BorderLayout.NORTH);
            allCVscroll.getViewport().add(allCvTextArea);
            p.add(allCVscroll, BorderLayout.CENTER);
        }
        tabbedPane.addTab("Tous les CV", p);

        p = new JPanel(new BorderLayout()); {
            JPanel q = new JPanel(new FlowLayout(FlowLayout.CENTER));
            {
                JPanel r = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                {
                    r.add(new JLabel("Numero du CV : "));
                }
                q.add(r);
                r = new JPanel(new FlowLayout(FlowLayout.CENTER));
                {
                    r.add(cvId);
                }
                q.add(r);
                r = new JPanel(new FlowLayout(FlowLayout.LEFT));
                {
                    r.add(show);
                }
                q.add(r);
            }
            p.add(q, BorderLayout.NORTH);
            cvScroll.getViewport().add(cvTextArea);
            p.add(cvScroll, BorderLayout.CENTER);
        }
        tabbedPane.addTab("Rechercher CV", p);

        formPanel = cvForm();
        formScroll.getViewport().add(formPanel);
        tabbedPane.addTab("Ajouter CV", formScroll);
        frame.add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel cvForm() {
        JPanel p;
        p = new JPanel(new GridLayout(0, 1)); {
            JPanel q = new JPanel(); {
                q.add(new JLabel("Nom :", SwingConstants.LEFT));
                q.add(cvFirstName);
            }
            p.add(q);
            q = new JPanel(); {
                q.add(new JLabel("Prenom :", SwingConstants.LEFT));
                q.add(cvName);
            }
            p.add(q);
            q = new JPanel(); {
                q.add(new JLabel("Objectifs :", SwingConstants.LEFT));
                q.add(cvObjectives);
            }
            p.add(q);
            q = new JPanel(); {
                q.add(new JLabel("Compétences :", SwingConstants.LEFT));
                q.add(cvSkill);
            }
            p.add(q);
            q = new JPanel(); {
                q.add(new JLabel("Formations :"));
                q.add(educationsList.get(0));
            }
            p.add(q);
            for (int i = 1; i < educationsList.size(); i++) {
                q = new JPanel(); {
                    p.add(new JLabel()); // Remplis l'espace
                    q.add(educationsList.get(i));
                }
                p.add(q);
            }
            q.add(addEducation);
            q = new JPanel(); {
                q.add(new JLabel("Expériences :"));
                q.add(experiencesList.get(0));
            }
            p.add(q);
            for (int i = 1; i < experiencesList.size(); i++) {
                q = new JPanel(); {
                    p.add(new JLabel()); // Remplis l'espace
                    q.add(experiencesList.get(i));
                }
                p.add(q);
            }
            q.add(addExperiences);
            q = new JPanel(); {
                q.add(new JLabel("Langues :"));
                q.add(langsList.get(0));
            }
            p.add(q);
            for (int i = 1; i < langsList.size(); i++) {
                q = new JPanel(); {
                    p.add(new JLabel()); // Remplis l'espace
                    q.add(langsList.get(i));
                }
                p.add(q);
            }
            q.add(addLang);
            q = new JPanel(); {
                q.add(new JLabel("Compétences Informatiques :"));
                q.add(skillsList.get(0));
            }
            p.add(q);
            for (int i = 1; i < skillsList.size(); i++) {
                q = new JPanel(); {
                    p.add(new JLabel()); // Remplis l'espace
                    q.add(skillsList.get(i));
                }
                p.add(q);
            }
            q.add(addITSkill);
            p.add(save);
        }
        return p;
    }

    private void redrawForm() {
        formScroll.remove(formPanel);
        formPanel = cvForm();
        formScroll.getViewport().add(formPanel);
    }

    private void createController() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        reload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allCvTextArea.setText("");
                getAllCV();
            }
        });

        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idCV = Integer.valueOf(cvId.getText());
                if (idCV >= 0) {
                    getCV(idCV);
                } else {
                    cvTextArea.setText("Id invalide.");
                }
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    putCV();
                } catch (JAXBException e1) {
                    // Erreur lors de l'enregistrement du CV.
                }
            }
        });

        addITSkill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                skillsList.add(new JTextField(20));
                redrawForm();
            }
        });

        addEducation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                educationsList.add(new JTextField(20));
                redrawForm();
            }
        });

        addLang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                langsList.add(new JTextField(20));
                redrawForm();
            }
        });

        addExperiences.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                experiencesList.add(new JTextField(20));
                redrawForm();
            }
        });

    }

    private void putCV() throws JAXBException {
        service = Service.create(QNAME);
        service.addPort(QNAME, HTTPBinding.HTTP_BINDING, URL);
        Dispatch<Source> dispatcher = service.createDispatch(QNAME,
                Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "PUT");

        Cv cv = new Cv();
        /*cv.setId(-1);
        List<Education> eduList = new ArrayList<Education>();
        for (JTextField edu : educationsList) {
            eduList.add(new Education("", "", ""));
        }
        cv.setEducations(eduList);
        List<Experience> expList = new ArrayList<Experience>();
        for (JTextField exp : experiencesList) {
            expList.add(new Experience("", "", ""));
        }
        cv.setExperiences(expList);
        cv.setFirstName(cvFirstName.getText());
        List<ITSkill> skList = new ArrayList<ITSkill>();
        for (JTextField sk : skillsList) {
            skList.add(new ITSkill("", 0));
        }
        cv.setItSkills(skList);
        List<Lang> lgList = new ArrayList<Lang>();
        for (JTextField sk : langsList) {
            lgList.add(new Lang("", 0));
        }
        cv.setLangs(lgList);
        cv.setLastName(cvName.getText());
        cv.setObjectives(cvObjectives.getText());
        cv.setSkills(cvSkill.getText());*/

        Source result = dispatcher.invoke(new JAXBSource(jc, cv));
        //printSource(result);
    }

    public static void printSource(Source s) {
        try {
            System.out.println("============================= Response Received =========================================");
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(s, new StreamResult(System.out));
            System.out.println("\n======================================================================");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String readCV(Document document) {
        String str = "";
        NodeList cv = document.getElementsByTagName("cv");
        int cvs = cv.getLength();
        for (int i = 0; i < cvs; i++) {
            Node item = cv.item(i);
            if(item.getNodeType() == Node.ELEMENT_NODE){
                Element itemEm = (Element) item;
                Node id = ((Element) itemEm).getElementsByTagName("id").item(0);
                str += "Identifiant : " +
                        id.getFirstChild().getNodeValue() + "\n";
                Node nom = ((Element) itemEm).getElementsByTagName("lastName").item(0);
                str += "Nom : " +
                        nom.getFirstChild().getNodeValue() + "\n";
                Node prenom = ((Element) itemEm).getElementsByTagName("firstName").item(0);
                str += "Prénom : " +
                        prenom.getFirstChild().getNodeValue() + "\n";
                Node objectifs = ((Element) itemEm).getElementsByTagName("objectives").item(0);
                str += "Objectifs : " +
                        objectifs.getFirstChild().getNodeValue() + "\n";
                Node competences = ((Element) itemEm).getElementsByTagName("skills").item(0);
                str += "Compétences : " +
                        competences.getFirstChild().getNodeValue() + "\n";

            }
            str += "\n\n";
        }
        return str;
    }

    private void getCV(int id) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);

        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            cvTextArea.setText("Impossible de créer le builder");
        }

        try {
            Document document = builder.parse(URL + "/" + id);
            document.getDocumentElement().normalize();
            cvTextArea.setText(readCV(document));
        } catch (Exception e) {
            cvTextArea.setText("Impossible de récupérer le CV " + id + ".");
        }
    }

    private void getAllCV() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);

        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            allCvTextArea.setText("Impossible de créer le builder");
        }

        try {
            Document document = builder.parse(URL);
            document.getDocumentElement().normalize();
            allCvTextArea.setText(readCV(document));
        } catch (Exception e) {
            allCvTextArea.setText("Impossible de récupérer les CVs.");
        }
    }

    public void display() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Client().display();
            }
        });
    }

}