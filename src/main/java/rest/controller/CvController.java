package rest.controller;

import org.springframework.stereotype.Controller;
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

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody CvArray getCvInXML() {
        List<Cv> cvList = new ArrayList<Cv>();
        cvList.add(new Cv("Dupon", "Jean", "Creer un CV"));
        cvList.add(new Cv("Dupon2", "Jean", "Creer une liste"));
        cvList.add(new Cv("Dernier", "CV", "Hehe"));
        CvArray list = new CvArray();
        list.cvList = cvList;
        return list;
    }
}
