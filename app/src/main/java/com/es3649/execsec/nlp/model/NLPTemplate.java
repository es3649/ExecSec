package com.es3649.execsec.nlp.model;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by es3649 on 5/26/19.
 *
 * Represents a template to be filled via NLP analysis
 */

public class NLPTemplate {

    /**
     * Created a new NLPTemplate
     */
    public NLPTemplate() {
        fields = new TreeMap<>();
    }

    /**
     * constructs a new Template from a json file
     * @param json the json containing the template
     * @return the template represented in the json
     */
    public static NLPTemplate fromJson(String json) {
        // TODO create a TemplateBuilder class that can be directly instantiated from json
        // TODO | this means we need a master TemplateField class which can represent any type of
        // TODO | <? extends TemplateField> and then can reduce to its correct type
        // TODO | And object of this type will be instantiated directly from the JSON, then it will
        // TODO | be condensed to an actual NLPTemplate using these builder/condensation methods.
        return null;
    }

    private Map<String, TemplateField> fields;

    /**
     * adds the field to the
     * @param tf the field to add to the template
     */
    public void addField(TemplateField tf) {
        fields.put(tf.getName(), tf);
    }

    /**
     * gets a field by name
     * @param name the name if the field to get
     * @return the field located at the given key
     */
    public TemplateField getField(String name) {
        return fields.get(name);
    }

    /**
     * removes the given field from the
     * @param name the name of the field to remove
     */
    public void removeField(String name) {
        fields.remove(name);
    }

    /**
     * @return the map in which the keys are stored
     */
    public Map<String, TemplateField> getFields() {
        return this.fields;
    }
}
