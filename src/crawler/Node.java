/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

/**
 *
 * @author Burhan
 */
public class Node<T> {
    T data;
    Node<T> next;
    Node<T> prev;
    
    Node (T data){
        this.data = data;
    }
    
}