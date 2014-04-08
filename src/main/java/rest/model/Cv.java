package rest.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Réprésente un CV.
 */
@XmlRootElement(name = "cv")
public class Cv {

    private String lastName;
    private String firstName;
    private String objectives;
    private List<String> experiences;
    private List<String> educations;
    private String skills;
    private List<Lang> langs;
    private List<ITskill> itSkills;

    public Cv() {}

    public Cv(
            String lastName,
            String firstName,
            String objectives,
            List<String> experiences,
            List<String> educations,
            String skills,
            List<Lang> langs,
            List<ITskill> itSkills) {
        this.firstName = firstName;
        this.objectives = objectives;
        this.lastName = lastName;
        this.experiences = experiences;
        this.educations = educations;
        this.skills = skills;
        this.langs = langs;
        this.itSkills = itSkills;
    }

    @XmlElement
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @XmlElement
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @XmlElement
    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        objectives = objectives;
    }

    @XmlElement
    public List<String> getEducations() {
        return educations;
    }

    public void setEducations(List<String> educations) {
        this.educations = educations;
    }

    @XmlElement
    public List<String> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<String> experiences) {
        this.experiences = experiences;
    }

    @XmlElement
    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    @XmlElement
    public List<Lang> getLangs() {
        return langs;
    }

    public void setLangs(List<Lang> langs) {
        this.langs = langs;
    }

    @XmlElement
    public List<ITskill> getItSkills() {
        return itSkills;
    }

    public void setItSkills(List<ITskill> itSkills) {
        this.itSkills = itSkills;
    }

    // Classe interne pour les langues
    public class Lang {
        private int level;
        private String langName;

        public Lang(String name, int level) {
            this.langName = name;
            this.level = level;
        }

        @XmlAttribute
        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        @XmlElement
        public String getLangName() {
            return langName;
        }

        public void setLangName(String langName) {
            this.langName = langName;
        }
    }

    // Classe interne pour les compétences informatiques
    public class ITskill {
        private int level;
        private String skillName;

        public ITskill(String name, int level) {
            this.skillName = name;
            this.level = level;
        }

        @XmlAttribute
        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        @XmlElement
        public String getSkillName() {
            return skillName;
        }

        public void setSkillName(String skillName) {
            this.skillName = skillName;
        }
    }
}
