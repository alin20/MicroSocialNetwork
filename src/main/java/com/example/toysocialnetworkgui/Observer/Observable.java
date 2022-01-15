package com.example.toysocialnetworkgui.Observer;

public interface Observable {
    void addObserver(Observer obs);
    void notifyObservers(UpdateType type);
}
