package com.es3649.execsec.nlp.model;

/**
 * Created by es3649 on 5/26/19.
 *
 * A polymorphic class which represents a field in an NLP template
 */

public abstract class TemplateField {

    /**
     * Sets these private fields of the TemplateField
     * @param name the name of the field
     * @param isRequired is it required that this field is filled in?
     */
    public TemplateField(String name, boolean isRequired) {
        this.setName(name);
        this.setRequired(isRequired);
        // if this is not required, we assume it's filled
        // otherwise, it's not filled yet
        this.isFilled = !isRequired;
    }

    private String name;
    private boolean isRequired;
    private boolean isFilled;

    public void setName(String name) {
        this.name = name;
    }

    public void setRequired(boolean required) {
        this.isRequired = required;
    }

    protected void setFilled(boolean fill) {
        this.isFilled = fill;
    }

    public String getName() {
        return this.name;
    }
    public boolean getRequired() {
        return this.isRequired;
    }

    public boolean getFilled() {
        return this.isFilled;
    }
}
