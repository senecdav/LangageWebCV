package rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rest.model.*;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<Experience> experiences = new ArrayList<Experience>();
        List<Education> educations = new ArrayList<Education>();
        List<Lang> langs = new ArrayList<Lang>();
        List<ITSkill> itSkills = new ArrayList<ITSkill>();

        experiences.add(new Experience("test", "test", "2014"));

        cvList.add(
            new Cv(
                0,
                "Dupon",
                "Jean",
                "Creer un CV",
                experiences,
                educations,
                "Typographie",
                langs,
                itSkills
            )
        );


        List<Experience> experiences2 = new ArrayList<Experience>();
        List<Education> educations2 = new ArrayList<Education>();
        List<Lang> langs2 = new ArrayList<Lang>();
        List<ITSkill> itSkills2 = new ArrayList<ITSkill>();

        experiences2.add(new Experience("Premiere Experience", "Premiere Experience", "2014"));
        experiences2.add(new Experience("Deuxieme Experience", "Deuxieme Experience", "2014"));
        educations2.add(new Education("BTS", "2009"));
        langs2.add(new Lang("Anglais", 2));
        itSkills2.add(new ITSkill("C", 1));

        cvList.add(
                new Cv(
                        1,
                        "Dupon2",
                        "Jean",
                        "Creer une liste",
                        experiences2,
                        educations2,
                        "Aucun",
                        langs2,
                        itSkills2
                )
        );
        /*cvList.add(new Cv("Dupon2", "Jean", "Creer une liste"));
        cvList.add(new Cv("Dernier", "CV", "Hehe"));*/
    }

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody CvArray getCvList() {
        CvArray list = new CvArray();
        list.cv = cvList;
        return list;
    }

    @RequestMapping(value="{id}", method = RequestMethod.GET)
    public @ResponseBody Cv getCvWithId(@PathVariable int id) {
        if (id < 0 || id >= cvList.size()) {
            return null;
        }
        return cvList.get(id);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody String putCv(@RequestBody Cv cv) {
        if (cv == null) {
           return "Erreur : Le CV n'est pas valide, il ne peux pas être vide !";
        }
        final boolean result = cvList.add(cv);
        cv.setId(cvList.size() - 1);
        if (result) {
            return "id:" + (cv.getId());
        } else {
            return  "Erreur : Le CV n'a pas pu être ajouté, veuillez contacter l'administrateur !";
        }
    }
}
