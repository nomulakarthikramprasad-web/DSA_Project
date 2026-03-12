import java.util.*;

// ============================================================
//  Property Price Trend Area Insights - DSA Implementation
//  Covers: Searching, Sorting, Linked List, Stack, Queue, Hashing
// ============================================================

// ── Data Model ───────────────────────────────────────────────
class Property {
    String id, name, type, area;
    double price;        // in Lakhs
    double growth;       // annual %
    int    pricePerSqft;
    double desirability;
    int    infrastructure, connectivity, demand;

    public Property(String id, String name, String type, String area,
                    double price, double growth, int pricePerSqft,
                    double desirability, int infra, int conn, int dem) {
        this.id = id; this.name = name; this.type = type; this.area = area;
        this.price = price; this.growth = growth;
        this.pricePerSqft = pricePerSqft; this.desirability = desirability;
        this.infrastructure = infra; this.connectivity = conn; this.demand = dem;
    }

    @Override public String toString() {
        return String.format("%-20s | Area: %-18s | Price: Rs %.1fL | Growth: +%.1f%% | Type: %-12s | Desirability: %.1f/10",
                name, area, price, growth, type, desirability);
    }
}

// ============================================================
//  1. LINKED LIST  – stores all properties
// ============================================================
class PropertyLinkedList {
    private static class Node {
        Property data;
        Node next;
        Node(Property p) { data = p; }
    }

    private Node head;
    private int size;

    // Insert at end
    public void insert(Property p) {
        Node n = new Node(p);
        if (head == null) { head = n; }
        else { Node t = head; while (t.next != null) t = t.next; t.next = n; }
        size++;
    }

    // Delete by id
    public boolean delete(String id) {
        if (head == null) return false;
        if (head.data.id.equals(id)) { head = head.next; size--; return true; }
        Node cur = head;
        while (cur.next != null) {
            if (cur.next.data.id.equals(id)) { cur.next = cur.next.next; size--; return true; }
            cur = cur.next;
        }
        return false;
    }

    // Search by id (linear)
    public Property searchById(String id) {
        Node cur = head;
        while (cur != null) { if (cur.data.id.equals(id)) return cur.data; cur = cur.next; }
        return null;
    }

    // Traverse → collect list
    public List<Property> toList() {
        List<Property> list = new ArrayList<>();
        Node cur = head;
        while (cur != null) { list.add(cur.data); cur = cur.next; }
        return list;
    }

    // Reverse linked list
    public void reverse() {
        Node prev = null, cur = head, next;
        while (cur != null) { next = cur.next; cur.next = prev; prev = cur; cur = next; }
        head = prev;
    }

    public int size() { return size; }

    public void display(String title) {
        System.out.println("\n" + "═".repeat(100));
        System.out.println("  " + title);
        System.out.println("═".repeat(100));
        if (head == null) { System.out.println("  (empty list)"); return; }
        Node cur = head; int i = 1;
        while (cur != null) { System.out.printf("  %2d. %s%n", i++, cur.data); cur = cur.next; }
        System.out.println("  Total: " + size + " properties");
    }
}

// ============================================================
//  2. STACK  – recently viewed properties (history)
// ============================================================
class ViewHistoryStack {
    private Property[] stack;
    private int top = -1;
    private static final int CAPACITY = 20;

    public ViewHistoryStack() { stack = new Property[CAPACITY]; }

    public void push(Property p) {
        if (top == CAPACITY - 1) { System.out.println("  [Stack] History full – oldest entry discarded."); return; }
        stack[++top] = p;
        System.out.println("  [Stack PUSH] Viewed: " + p.name);
    }

    public Property pop() {
        if (isEmpty()) { System.out.println("  [Stack] No history."); return null; }
        return stack[top--];
    }

    public Property peek() { return isEmpty() ? null : stack[top]; }
    public boolean isEmpty() { return top == -1; }

    public void displayHistory() {
        System.out.println("\n── View History (Stack – LIFO) " + "─".repeat(50));
        if (isEmpty()) { System.out.println("  No history."); return; }
        for (int i = top; i >= 0; i--)
            System.out.printf("  [%2d] %s%n", (top - i + 1), stack[i]);
    }

    // Parentheses / bracket checker using stack (expression evaluation application)
    public static boolean checkBrackets(String expr) {
        Deque<Character> s = new ArrayDeque<>();
        for (char c : expr.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') s.push(c);
            else if (c == ')' || c == ']' || c == '}') {
                if (s.isEmpty()) return false;
                char top2 = s.pop();
                if ((c == ')' && top2 != '(') || (c == ']' && top2 != '[') || (c == '}' && top2 != '{'))
                    return false;
            }
        }
        return s.isEmpty();
    }
}

// ============================================================
//  3. QUEUE  – price alert / pending notifications
// ============================================================
class PriceAlertQueue {
    private static class Alert {
        String area; double threshold; String message;
        Alert(String a, double t, String m) { area=a; threshold=t; message=m; }
        public String toString() { return String.format("Area: %-18s | Threshold: Rs %.1fL | %s", area, threshold, message); }
    }

    private Alert[] queue;
    private int front = 0, rear = -1, count = 0;
    private static final int CAPACITY = 10;

    public PriceAlertQueue() { queue = new Alert[CAPACITY]; }

    public void enqueue(String area, double threshold, String msg) {
        if (count == CAPACITY) { System.out.println("  [Queue] Alert queue full."); return; }
        rear = (rear + 1) % CAPACITY;
        queue[rear] = new Alert(area, threshold, msg);
        count++;
        System.out.println("  [Queue ENQUEUE] Alert set → " + area + " @ Rs " + threshold + "L");
    }

    public Alert dequeue() {
        if (count == 0) { System.out.println("  [Queue] No pending alerts."); return null; }
        Alert a = queue[front];
        front = (front + 1) % CAPACITY;
        count--;
        return a;
    }

    public boolean isEmpty() { return count == 0; }

    public void displayAlerts() {
        System.out.println("\n── Price Alert Queue (Circular Queue – FIFO) " + "─".repeat(35));
        if (count == 0) { System.out.println("  No pending alerts."); return; }
        int idx = front;
        for (int i = 0; i < count; i++) {
            System.out.printf("  [%d] %s%n", i+1, queue[idx]);
            idx = (idx + 1) % CAPACITY;
        }
        System.out.println("  Queue size: " + count + "/" + CAPACITY);
    }
}

// ============================================================
//  4. HASH TABLE  – O(1) property lookup by area
// ============================================================
class PropertyHashTable {
    private static final int TABLE_SIZE = 16;
    private List<Property>[] table;

    @SuppressWarnings("unchecked")
    public PropertyHashTable() {
        table = new ArrayList[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++) table[i] = new ArrayList<>();
    }

    private int hash(String key) {
        int h = 0;
        for (char c : key.toLowerCase().toCharArray()) h = (h * 31 + c) % TABLE_SIZE;
        return Math.abs(h);
    }

    public void insert(Property p) {
        int idx = hash(p.area);
        table[idx].add(p);
    }

    public List<Property> searchByArea(String area) {
        return table[hash(area)];
    }

    public boolean deleteById(String id) {
        for (List<Property> bucket : table)
            if (bucket.removeIf(p -> p.id.equals(id))) return true;
        return false;
    }

    public void display() {
        System.out.println("\n── Hash Table (Chaining – Area Index) " + "─".repeat(42));
        for (int i = 0; i < TABLE_SIZE; i++) {
            if (!table[i].isEmpty()) {
                System.out.printf("  Bucket[%2d] → ", i);
                for (Property p : table[i]) System.out.print(p.name + " | ");
                System.out.println();
            }
        }
    }
}

// ============================================================
//  5. SEARCHING  – Linear + Binary Search on price
// ============================================================
class SearchEngine {

    // Linear Search by area name (case-insensitive)
    public static List<Property> linearSearchByArea(List<Property> props, String area) {
        List<Property> result = new ArrayList<>();
        int comparisons = 0;
        for (Property p : props) {
            comparisons++;
            if (p.area.equalsIgnoreCase(area)) result.add(p);
        }
        System.out.printf("  [Linear Search] Comparisons made: %d%n", comparisons);
        return result;
    }

    // Binary Search by price (list must be sorted by price first)
    public static int binarySearchByPrice(List<Property> sorted, double targetPrice) {
        int lo = 0, hi = sorted.size() - 1, comparisons = 0;
        while (lo <= hi) {
            comparisons++;
            int mid = (lo + hi) / 2;
            double midPrice = sorted.get(mid).price;
            if (Math.abs(midPrice - targetPrice) < 0.01) {
                System.out.printf("  [Binary Search] Found at index %d after %d comparisons.%n", mid, comparisons);
                return mid;
            } else if (midPrice < targetPrice) lo = mid + 1;
            else hi = mid - 1;
        }
        System.out.printf("  [Binary Search] Not found. %d comparisons made.%n", comparisons);
        return -1;
    }
}

// ============================================================
//  6. SORTING ALGORITHMS
// ============================================================
class SortEngine {

    // Bubble Sort by price (ascending) – O(n²)
    public static void bubbleSort(List<Property> list) {
        int n = list.size(), passes = 0;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).price > list.get(j + 1).price) {
                    Collections.swap(list, j, j + 1);
                    swapped = true;
                }
            }
            passes++;
            if (!swapped) break;
        }
        System.out.printf("  [Bubble Sort] Completed in %d passes. O(n²)%n", passes);
    }

    // Insertion Sort by growth rate (descending) – O(n²)
    public static void insertionSortByGrowth(List<Property> list) {
        int n = list.size();
        for (int i = 1; i < n; i++) {
            Property key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).growth < key.growth) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
        System.out.println("  [Insertion Sort] Sorted by growth (desc). O(n²)");
    }

    // Selection Sort by desirability – O(n²)
    public static void selectionSortByDesirability(List<Property> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            int maxIdx = i;
            for (int j = i + 1; j < n; j++)
                if (list.get(j).desirability > list.get(maxIdx).desirability) maxIdx = j;
            Collections.swap(list, i, maxIdx);
        }
        System.out.println("  [Selection Sort] Sorted by desirability (desc). O(n²)");
    }

    // Merge Sort by price – O(n log n)
    public static List<Property> mergeSort(List<Property> list) {
        if (list.size() <= 1) return list;
        int mid = list.size() / 2;
        List<Property> left  = mergeSort(new ArrayList<>(list.subList(0, mid)));
        List<Property> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())));
        return merge(left, right);
    }
    private static List<Property> merge(List<Property> l, List<Property> r) {
        List<Property> res = new ArrayList<>();
        int i = 0, j = 0;
        while (i < l.size() && j < r.size()) {
            if (l.get(i).price <= r.get(j).price) res.add(l.get(i++));
            else res.add(r.get(j++));
        }
        while (i < l.size()) res.add(l.get(i++));
        while (j < r.size()) res.add(r.get(j++));
        return res;
    }

    // Quick Sort by pricePerSqft – O(n log n) average
    public static void quickSort(List<Property> list, int lo, int hi) {
        if (lo < hi) {
            int pivot = partition(list, lo, hi);
            quickSort(list, lo, pivot - 1);
            quickSort(list, pivot + 1, hi);
        }
    }
    private static int partition(List<Property> list, int lo, int hi) {
        int pivot = list.get(hi).pricePerSqft;
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (list.get(j).pricePerSqft <= pivot) { i++; Collections.swap(list, i, j); }
        }
        Collections.swap(list, i + 1, hi);
        return i + 1;
    }
}

// ============================================================
//  MAIN – Interactive Terminal Menu
// ============================================================
public class PropertyPriceAnalyzer {

    static PropertyLinkedList propertyList = new PropertyLinkedList();
    static PropertyHashTable  hashTable    = new PropertyHashTable();
    static ViewHistoryStack   history      = new ViewHistoryStack();
    static PriceAlertQueue    alertQueue   = new PriceAlertQueue();
    static Scanner            sc           = new Scanner(System.in);

    public static void main(String[] args) {
        loadSampleData();
        System.out.println("\n" + "█".repeat(100));
        System.out.println("█" + " ".repeat(30) + "PROPERTY PRICE TREND AREA INSIGHTS " + " ".repeat(19) + "█");
        System.out.println("█".repeat(100));

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("  Enter choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1"  -> showAllProperties();
                case "2"  -> demonstrateSorting();
                case "3"  -> demonstrateSearching();
                case "4"  -> demonstrateLinkedList();
                case "5"  -> demonstrateStack();
                case "6"  -> demonstrateQueue();
                case "7"  -> demonstrateHashing();
                case "8"  -> showMarketInsights();
                case "9"  -> compareAreas();
                case "10" -> addProperty();
                case "0"  -> { System.out.println("\n  Goodbye!\n"); running = false; }
                default   -> System.out.println("  Invalid choice.");
            }
        }
    }

    static void printMenu() {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("  MAIN MENU");
        System.out.println("─".repeat(60));
        System.out.println("  1.  View All Properties (Linked List Traversal)");
        System.out.println("  2.  Sorting Demo   (Bubble / Insertion / Selection / Merge / Quick)");
        System.out.println("  3.  Search Demo    (Linear + Binary Search)");
        System.out.println("  4.  Linked List    (Insert / Delete / Reverse)");
        System.out.println("  5.  Stack Demo     (View History + Bracket Checker)");
        System.out.println("  6.  Queue Demo     (Price Alerts)");
        System.out.println("  7.  Hash Table     (Area Lookup)");
        System.out.println("  8.  Market Insights");
        System.out.println("  9.  Compare Two Areas");
        System.out.println("  10. Add New Property");
        System.out.println("  0.  Exit");
        System.out.println("─".repeat(60));
    }

    // ── Show All ─────────────────────────────────────────────
    static void showAllProperties() {
        propertyList.display("ALL PROPERTIES (Linked List Traversal)");
    }

    // ── Sorting Demo ─────────────────────────────────────────
    static void demonstrateSorting() {
        List<Property> copy = new ArrayList<>(propertyList.toList());
        System.out.println("\n" + "═".repeat(100));
        System.out.println("  SORTING ALGORITHMS DEMO");
        System.out.println("═".repeat(100));

        // Bubble Sort
        List<Property> bubble = new ArrayList<>(copy);
        SortEngine.bubbleSort(bubble);
        System.out.println("  Sorted by Price (Bubble Sort, ascending):");
        bubble.forEach(p -> System.out.printf("    Rs %.1fL – %s%n", p.price, p.name));

        // Insertion Sort
        List<Property> ins = new ArrayList<>(copy);
        SortEngine.insertionSortByGrowth(ins);
        System.out.println("\n  Sorted by Growth (Insertion Sort, descending):");
        ins.forEach(p -> System.out.printf("    +%.1f%% – %s%n", p.growth, p.name));

        // Selection Sort
        List<Property> sel = new ArrayList<>(copy);
        SortEngine.selectionSortByDesirability(sel);
        System.out.println("\n  Sorted by Desirability (Selection Sort, descending):");
        sel.forEach(p -> System.out.printf("    %.1f/10 – %s%n", p.desirability, p.name));

        // Merge Sort
        List<Property> merged = SortEngine.mergeSort(new ArrayList<>(copy));
        System.out.println("\n  Sorted by Price (Merge Sort, O(n log n)):");
        merged.forEach(p -> System.out.printf("    Rs %.1fL – %s%n", p.price, p.name));

        // Quick Sort
        List<Property> quick = new ArrayList<>(copy);
        SortEngine.quickSort(quick, 0, quick.size() - 1);
        System.out.println("\n  Sorted by Price/SqFt (Quick Sort, O(n log n) avg):");
        quick.forEach(p -> System.out.printf("    Rs %d/sqft – %s%n", p.pricePerSqft, p.name));

        System.out.println("\n  Time Complexity Summary:");
        System.out.println("  ┌─────────────────┬──────────┬──────────┬────────────┐");
        System.out.println("  │ Algorithm       │   Best   │  Average │   Worst    │");
        System.out.println("  ├─────────────────┼──────────┼──────────┼────────────┤");
        System.out.println("  │ Bubble Sort     │  O(n)    │  O(n²)   │   O(n²)    │");
        System.out.println("  │ Insertion Sort  │  O(n)    │  O(n²)   │   O(n²)    │");
        System.out.println("  │ Selection Sort  │  O(n²)   │  O(n²)   │   O(n²)    │");
        System.out.println("  │ Merge Sort      │ O(nlogn) │ O(nlogn) │  O(nlogn)  │");
        System.out.println("  │ Quick Sort      │ O(nlogn) │ O(nlogn) │   O(n²)    │");
        System.out.println("  └─────────────────┴──────────┴──────────┴────────────┘");
    }

    // ── Searching Demo ───────────────────────────────────────
    static void demonstrateSearching() {
        System.out.println("\n" + "═".repeat(100));
        System.out.println("  SEARCHING ALGORITHMS DEMO");
        System.out.println("═".repeat(100));

        // Linear Search
        System.out.print("  Enter area to search (e.g. Downtown): ");
        String area = sc.nextLine().trim();
        List<Property> result = SearchEngine.linearSearchByArea(propertyList.toList(), area);
        if (result.isEmpty()) System.out.println("  No properties found in: " + area);
        else result.forEach(p -> System.out.println("  FOUND → " + p));

        // Binary Search (sort first)
        List<Property> sorted = SortEngine.mergeSort(propertyList.toList());
        System.out.print("\n  Enter price (in Lakhs) for Binary Search: Rs ");
        try {
            double price = Double.parseDouble(sc.nextLine().trim());
            int idx = SearchEngine.binarySearchByPrice(sorted, price);
            if (idx >= 0) System.out.println("  FOUND → " + sorted.get(idx));
            else System.out.println("  No property found at Rs " + price + "L");
        } catch (NumberFormatException e) { System.out.println("  Invalid input."); }
    }

    // ── Linked List Demo ──────────────────────────────────────
    static void demonstrateLinkedList() {
        System.out.println("\n" + "═".repeat(100));
        System.out.println("  LINKED LIST OPERATIONS");
        System.out.println("═".repeat(100));
        System.out.println("  a) Reverse List   b) Delete by ID   c) Back");
        System.out.print("  Choice: ");
        String c = sc.nextLine().trim().toLowerCase();
        switch (c) {
            case "a" -> {
                propertyList.reverse();
                System.out.println("  List reversed!");
                propertyList.display("Reversed Linked List");
                propertyList.reverse(); // restore
            }
            case "b" -> {
                System.out.print("  Enter property ID to delete: ");
                String id = sc.nextLine().trim();
                System.out.println(propertyList.delete(id) ? "  Deleted successfully." : "  ID not found.");
            }
            default -> System.out.println("  Returning to menu.");
        }
    }

    // ── Stack Demo ────────────────────────────────────────────
    static void demonstrateStack() {
        System.out.println("\n" + "═".repeat(100));
        System.out.println("  STACK DEMO – View History & Expression Checker");
        System.out.println("═".repeat(100));

        // Push some views
        List<Property> props = propertyList.toList();
        if (props.size() > 0) history.push(props.get(0));
        if (props.size() > 1) history.push(props.get(1));
        if (props.size() > 2) history.push(props.get(2));
        history.displayHistory();

        System.out.println("\n  Parentheses Checker (Stack Application):");
        String[] exprs = {"(Rs 45L + [12%])", "({Rs 52.5L})", "[(Rs 68.9L])", "((Rs 38.2L))"};
        for (String expr : exprs)
            System.out.printf("  %s  →  %s%n", expr,
                ViewHistoryStack.checkBrackets(expr) ? "✔ Balanced" : "✘ NOT balanced");
    }

    // ── Queue Demo ────────────────────────────────────────────
    static void demonstrateQueue() {
        System.out.println("\n" + "═".repeat(100));
        System.out.println("  QUEUE DEMO – Price Alerts (Circular Queue)");
        System.out.println("═".repeat(100));

        alertQueue.enqueue("Downtown",          50.0, "ALERT: Price crossed Rs 50L");
        alertQueue.enqueue("Waterfront",        65.0, "ALERT: Price crossed Rs 65L");
        alertQueue.enqueue("Suburban",          37.0, "ALERT: Price crossed Rs 37L");
        alertQueue.enqueue("Historic District", 40.0, "ALERT: Price crossed Rs 40L");
        alertQueue.displayAlerts();

        System.out.println("\n  Processing first alert (Dequeue)...");
        var alert = alertQueue.dequeue();
        if (alert != null) System.out.println("  Processed → " + alert);
        alertQueue.displayAlerts();
    }

    // ── Hash Table Demo ──────────────────────────────────────
    static void demonstrateHashing() {
        System.out.println("\n" + "═".repeat(100));
        System.out.println("  HASH TABLE DEMO – Area-based Property Lookup");
        System.out.println("═".repeat(100));
        hashTable.display();

        System.out.print("\n  Enter area to lookup: ");
        String area = sc.nextLine().trim();
        List<Property> found = hashTable.searchByArea(area);
        if (found.isEmpty()) System.out.println("  No properties found.");
        else { System.out.println("  Found " + found.size() + " result(s):"); found.forEach(p -> System.out.println("  → " + p)); }
    }

    // ── Market Insights ───────────────────────────────────────
    static void showMarketInsights() {
        List<Property> props = propertyList.toList();
        double avgPrice  = props.stream().mapToDouble(p -> p.price).average().orElse(0);
        double avgGrowth = props.stream().mapToDouble(p -> p.growth).average().orElse(0);
        Property topGrowth = props.stream().max(Comparator.comparingDouble(p -> p.growth)).orElse(null);
        Property topDesk   = props.stream().max(Comparator.comparingDouble(p -> p.desirability)).orElse(null);

        System.out.println("\n" + "═".repeat(100));
        System.out.println("  MARKET INSIGHTS & SCORECARD");
        System.out.println("═".repeat(100));
        System.out.printf("  %-30s Rs %.2f Lakhs%n", "Average Property Price:", avgPrice);
        System.out.printf("  %-30s +%.2f%%%n", "Average Market Growth:", avgGrowth);
        System.out.printf("  %-30s %s (+%.1f%%)%n", "Top Growth Area:", topGrowth != null ? topGrowth.area : "N/A", topGrowth != null ? topGrowth.growth : 0);
        System.out.printf("  %-30s %s (%.1f/10)%n", "Most Desirable:", topDesk != null ? topDesk.name : "N/A", topDesk != null ? topDesk.desirability : 0);

        System.out.println("\n  Investment Recommendations:");
        props.stream().sorted(Comparator.comparingDouble((Property p) -> p.growth).reversed())
             .forEach(p -> {
                 String tag = p.growth > 10 ? "★★★ STRONG BUY" : p.growth > 7 ? "★★  HOLD/BUY" : "★   MONITOR";
                 System.out.printf("  %-35s | Rs %.1fL | Growth: +%.1f%% | %s%n",
                     p.name, p.price, p.growth, tag);
             });
    }

    // ── Compare Areas ─────────────────────────────────────────
    static void compareAreas() {
        List<Property> all = propertyList.toList();
        Set<String> areas = new LinkedHashSet<>();
        all.forEach(p -> areas.add(p.area));
        System.out.println("\n  Available areas: " + areas);
        System.out.print("  Area 1: "); String a1 = sc.nextLine().trim();
        System.out.print("  Area 2: "); String a2 = sc.nextLine().trim();

        List<Property> g1 = SearchEngine.linearSearchByArea(all, a1);
        List<Property> g2 = SearchEngine.linearSearchByArea(all, a2);

        System.out.println("\n  " + "─".repeat(80));
        System.out.printf("  %-40s %-40s%n", a1.toUpperCase(), a2.toUpperCase());
        System.out.println("  " + "─".repeat(80));

        OptionalDouble avg1 = g1.stream().mapToDouble(p->p.price).average();
        OptionalDouble avg2 = g2.stream().mapToDouble(p->p.price).average();
        System.out.printf("  Avg Price:  Rs %-33s Rs %.2fL%n",
            avg1.isPresent() ? String.format("%.2fL", avg1.getAsDouble()) : "N/A", avg2.orElse(0));

        OptionalDouble gr1 = g1.stream().mapToDouble(p->p.growth).average();
        OptionalDouble gr2 = g2.stream().mapToDouble(p->p.growth).average();
        System.out.printf("  Avg Growth: %-35s %.2f%%%n",
            gr1.isPresent() ? String.format("+%.2f%%", gr1.getAsDouble()) : "N/A", gr2.orElse(0));

        System.out.printf("  Listings:   %-35s %d%n", g1.size(), g2.size());
    }

    // ── Add Property ─────────────────────────────────────────
    static void addProperty() {
        System.out.println("\n  Add New Property");
        try {
            System.out.print("  ID: "); String id = sc.nextLine().trim();
            System.out.print("  Name: "); String name = sc.nextLine().trim();
            System.out.print("  Type (Apartment/House/Commercial): "); String type = sc.nextLine().trim();
            System.out.print("  Area: "); String area = sc.nextLine().trim();
            System.out.print("  Price (Lakhs): "); double price = Double.parseDouble(sc.nextLine().trim());
            System.out.print("  Annual Growth %: "); double growth = Double.parseDouble(sc.nextLine().trim());
            System.out.print("  Price/SqFt: "); int psf = Integer.parseInt(sc.nextLine().trim());

            Property p = new Property(id, name, type, area, price, growth, psf, 8.0, 80, 80, 80);
            propertyList.insert(p);
            hashTable.insert(p);
            System.out.println("  ✔ Property added successfully!");
        } catch (Exception e) { System.out.println("  Invalid input: " + e.getMessage()); }
    }

    // ── Load Sample Data ─────────────────────────────────────
    static void loadSampleData() {
        Property[] sample = {
            new Property("P001","Skyline Towers","Apartment","Downtown",         52.5,12.8,1250,9.2,95,90,92),
            new Property("P002","Park Residency","House",    "Downtown",         61.0,11.5,1400,9.0,93,88,90),
            new Property("P003","Green Valley",  "House",    "Suburban Zone",   38.2,10.5, 850,8.1,75,80,85),
            new Property("P004","Metro Homes",   "Apartment","Suburban Zone",   35.5, 9.8, 800,7.9,72,78,82),
            new Property("P005","Blue Lagoon",   "Apartment","Waterfront",      68.9,10.9,1800,9.5,88,85,88),
            new Property("P006","Sea Breeze",    "House",    "Waterfront",      75.0,11.2,2100,9.7,90,87,91),
            new Property("P007","Heritage Inn",  "Apartment","Historic District",42.1, 4.7, 920,7.8,70,75,65),
            new Property("P008","Old Town Villas","House",   "Historic District",48.0, 5.1,1050,8.0,68,72,68),
            new Property("P009","Tech Hub Suites","Commercial","Downtown",       80.0,13.1,1600,8.8,97,92,95),
            new Property("P010","Riverside Apts","Apartment","Waterfront",      58.5, 9.5,1500,8.9,85,83,85),
        };
        for (Property p : sample) {
            propertyList.insert(p);
            hashTable.insert(p);
        }
        System.out.println("  Loaded " + sample.length + " sample properties.\n");
    }
}
