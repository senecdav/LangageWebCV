package rest.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import rest.model.*;

/**
 * Created by senecdav on 01/04/14.
 */
@XmlRootElement(name = "listcv")
public class CvArray {

    public List<Cv> cvList;
}
