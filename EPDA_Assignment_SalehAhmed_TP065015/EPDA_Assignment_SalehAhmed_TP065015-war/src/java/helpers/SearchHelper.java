/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author saleh
 */
public class SearchHelper {
    
    public static List<Map<String, String>> search(List<Map<String, String>> data, String text) {
        if (text == null || text.isEmpty()) {
            return data;
        }
        return data.stream()
                .filter(map -> String.join(",", map.values().toArray(new String[0])).trim().toLowerCase().contains(text.trim().toLowerCase()))
                .collect(Collectors.toList());
    }
    
}