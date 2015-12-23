/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.xenonraite.fileservice;

/**
 *
 * @author Sontik
 */
public class ListIds {
    int[] ids;
    public ListIds(){
        
    }
    
    public ListIds(int[] ids){
        this.ids = ids;
        
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int id: ids){
            sb.append("{"+id+"}");
        }
        return sb.toString();
    }
    
    
    
}
