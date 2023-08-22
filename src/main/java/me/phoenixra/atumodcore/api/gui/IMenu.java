package me.phoenixra.atumodcore.api.gui;

public interface IMenu {

     void setUsable(boolean b);

     boolean isUsable();

     void closeMenu();

     boolean isOpen();

}