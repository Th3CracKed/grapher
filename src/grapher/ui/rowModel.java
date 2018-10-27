/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grapher.ui;

import grapher.fc.Function;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ColorPicker;

/**
 *
 * @author Asus
 */
public class rowModel {
    private Function function;
    private ColorPicker color;

    public rowModel(Function function, ColorPicker color) {
        this.function = function;
        this.color = color;
    }
    
    public Function getFunction() {
        return function;
    }
    
    public StringProperty getFunctionProperty() { 
         return new SimpleStringProperty(function.toString());
     }

    public void setFunction(Function function) {
        this.function = function;
    }

    public ColorPicker getColor() {
        return color;
    }

    public void setColor(ColorPicker color) {
        this.color = color;
    }
    
}
