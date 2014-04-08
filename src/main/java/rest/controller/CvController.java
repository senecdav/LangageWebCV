package rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import rest.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by senecdav on 01/04/14.
 */
@Controller
@RequestMapping("/resume")
public class CvController {

    private static List<Cv> cvList = new ArrayList<Cv>();

    //Charge quelques cv au démarrage
    static {
        cvList.add(new Cv("Dupon", "Jean", "Creer un CV"));
        cvList.add(new Cv("Dupon2", "Jean", "Creer une liste"));
        cvList.add(new Cv("Dernier", "CV", "Hehe"));
    }

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody CvArray getCvInXML() {
        CvArray list = new CvArray();
        list.cvList = cvList;
        return list;
    }

    @RequestMapping(value="{id}", method = RequestMethod.GET)
    public @ResponseBody Cv getCvWithId(@PathVariable int id) {
        if (id < 0 || id > cvList.size()) {
            return null;
        }
        return cvList.get(id);
    }

    @RequestMapping(value="{cv}", method = RequestMethod.PUT)
    public @ResponseBody String putCv(@PathVariable Cv cv) {
        if (cv == null) {
           return "Erreur : Le CV ne peux pas être vide !";
        }
        final boolean result = cvList.add(cv);
        if (result) {
            return "Succès : Le CV à été ajouté";
        } else {
            return  "Erreur : Le CV n'a pas pu être ajouté, veuillez contacter l'administrateur !";
        }
    }
}
