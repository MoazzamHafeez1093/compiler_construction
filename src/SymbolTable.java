import java.util.*;

/**
 * SymbolTable.java
 * Maintains a table of all identifiers found during scanning
 * Tracks their type, first occurrence location, and frequency
 */
public class SymbolTable {
    
    /**
     * Inner class to store information about each identifier
     */
    private class SymbolInfo {
        String name;
        String type;              // Will be determined in semantic analysis
        int firstLine;
        int firstColumn;
        int frequency;
        
        SymbolInfo(String name, int line, int column) {
            this.name = name;
            this.type = "undeclared";  // Type will be set later during semantic analysis
            this.firstLine = line;
            this.firstColumn = column;
            this.frequency = 1;
        }
        
        void incrementFrequency() {
            frequency++;
        }
        
        @Override
        public String toString() {
            return String.format("%-20s | %-15s | Line: %-4d Col: %-4d | Frequency: %d",
                               name, type, firstLine, firstColumn, frequency);
        }
    }
    
    // Use LinkedHashMap to maintain insertion order
    private Map<String, SymbolInfo> table;
    
    public SymbolTable() {
        table = new LinkedHashMap<>();
    }
    
    /**
     * Add an identifier to the symbol table or increment its frequency
     */
    public void addIdentifier(String name, int line, int column) {
        if (table.containsKey(name)) {
            // Identifier already exists, just increment frequency
            table.get(name).incrementFrequency();
        } else {
            // New identifier, add to table
            table.put(name, new SymbolInfo(name, line, column));
        }
    }
    
    /**
     * Check if an identifier exists in the symbol table
     */
    public boolean contains(String name) {
        return table.containsKey(name);
    }
    
    /**
     * Get the frequency of an identifier
     */
    public int getFrequency(String name) {
        SymbolInfo info = table.get(name);
        return (info != null) ? info.frequency : 0;
    }
    
    /**
     * Get total number of unique identifiers
     */
    public int getSize() {
        return table.size();
    }
    
    /**
     * Print the symbol table in a formatted way
     */
    public void display() {
        System.out.println("\n" + "=".repeat(85));
        System.out.println("SYMBOL TABLE");
        System.out.println("=".repeat(85));
        
        if (table.isEmpty()) {
            System.out.println("No identifiers found.");
        } else {
            System.out.println(String.format("%-20s | %-15s | %-15s | %s",
                             "Identifier", "Type", "First Occurrence", "Frequency"));
            System.out.println("-".repeat(85));
            
            for (SymbolInfo info : table.values()) {
                System.out.println(info);
            }
            
            System.out.println("-".repeat(85));
            System.out.println("Total unique identifiers: " + table.size());
        }
        System.out.println("=".repeat(85) + "\n");
    }
    
    /**
     * Get all identifiers sorted by frequency (for statistics)
     */
    public List<Map.Entry<String, SymbolInfo>> getIdentifiersByFrequency() {
        List<Map.Entry<String, SymbolInfo>> entries = new ArrayList<>(table.entrySet());
        entries.sort((a, b) -> Integer.compare(b.getValue().frequency, a.getValue().frequency));
        return entries;
    }
}
