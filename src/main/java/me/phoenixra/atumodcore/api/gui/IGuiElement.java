package me.phoenixra.atumodcore.api.gui;

public interface IGuiElement {
    int getX();
    int getY();
    int getWidth();
    int getHeight();


    default boolean isMouseInElement(int mouseX, int mouseY){
        return (mouseX >= getX()) && (mouseX <= getX() + getWidth()) && (mouseY >= getY()) && mouseY <= getY() + getHeight();
    }
}
