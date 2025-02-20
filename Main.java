import java.util.*;

class State {
    int id;
    boolean isFinal;
    Map<String, State> transitions;

    public State(int id) {
        this.id = id;
        this.isFinal = false;
        this.transitions = new HashMap<>();
    }

    public void addTransition(String symbol, State nextState) {
        transitions.put(symbol, nextState);
    }

    @Override
    public String toString() {
        return "State " + id + (isFinal ? " (Final)" : "");
    }
}

class NFA {
    private State startState;
    private Set<State> states;

    public NFA(State startState, Set<State> states) {
        this.startState = startState;
        this.states = states;
    }

    public State getStartState() {
        return startState;
    }

    public Set<State> getStates() {
        return states;
    }

    public void displayTransitionTable() {
        System.out.println("\n📌 NFA Transition Table:");
        System.out.println("State  |  Symbol  |  Next State");
        System.out.println("---------------------------------");

        for (State state : states) {
            for (Map.Entry<String, State> entry : state.transitions.entrySet()) {
                System.out.println("  " + state.id + "     |   " + entry.getKey() + "   |   " + entry.getValue().id);
            }
        }
    }
}

class DFA {
    private State startState;
    private Set<State> states;

    public DFA(State startState, Set<State> states) {
        this.startState = startState;
        this.states = states;
    }

    public void displayTransitionTable() {
        System.out.println("\n📌 DFA Transition Table:");
        System.out.println("State  |  Symbol  |  Next State");
        System.out.println("---------------------------------");

        for (State state : states) {
            for (Map.Entry<String, State> entry : state.transitions.entrySet()) {
                System.out.println("  " + state.id + "     |   " + entry.getKey() + "   |   " + entry.getValue().id);
            }
        }
    }

    public int getTotalStates() {
        return states.size();
    }
}

class NFAToDFAConverter {
    private int stateCounter = 0;

    public DFA convert(NFA nfa) {
        Map<Set<State>, State> dfaStates = new HashMap<>();
        Queue<Set<State>> queue = new LinkedList<>();
        Set<Set<State>> visited = new HashSet<>();

        Set<State> startSet = Set.of(nfa.getStartState());
        State startState = new State(stateCounter++);
        dfaStates.put(startSet, startState);
        queue.add(startSet);
        visited.add(startSet);

        while (!queue.isEmpty()) {
            Set<State> currentSet = queue.poll();
            State dfaState = dfaStates.get(currentSet);

            Map<String, Set<State>> newTransitions = new HashMap<>();
            for (State nfaState : currentSet) {
                for (Map.Entry<String, State> entry : nfaState.transitions.entrySet()) {
                    String symbol = entry.getKey();
                    newTransitions.computeIfAbsent(symbol, k -> new HashSet<>()).add(entry.getValue());
                }
            }

            for (Map.Entry<String, Set<State>> entry : newTransitions.entrySet()) {
                String symbol = entry.getKey();
                Set<State> targetSet = entry.getValue();

                if (!visited.contains(targetSet)) {
                    visited.add(targetSet);
                    queue.add(targetSet);
                }

                State targetState = dfaStates.computeIfAbsent(targetSet, k -> new State(stateCounter++));
                dfaState.addTransition(symbol, targetState);
            }
        }

        return new DFA(dfaStates.get(startSet), new HashSet<>(dfaStates.values()));
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("\n🔹 Generating NFA for Language...");

        // Define states
        State startState = new State(0);
        State openingBracketState = new State(1);
        State grabState = new State(2);
        State grabIdentifierState = new State(3);
        State grabEndState = new State(4);
        State shoutState = new State(5);
        State shoutIdentifierState = new State(6);
        State shoutEndState = new State(7);
        State numbaState = new State(8);
        State numbaAssignState = new State(9);
        State numbaValueState = new State(10);
        State charyState = new State(11);
        State charyAssignState = new State(12);
        State charyValueState = new State(13);
        State arithmeticOpState = new State(14);
        State closingBracketState = new State(15);
        closingBracketState.isFinal = true;

        // Define transitions
        startState.addTransition("{", openingBracketState);
        openingBracketState.addTransition("grab", grabState);
        grabState.addTransition("identifier", grabIdentifierState);
        grabIdentifierState.addTransition(";", grabEndState);

        openingBracketState.addTransition("shout", shoutState);
        shoutState.addTransition("identifier", shoutIdentifierState);
        shoutIdentifierState.addTransition(";", shoutEndState);

        openingBracketState.addTransition("numba", numbaState);
        numbaState.addTransition("=", numbaAssignState);
        numbaAssignState.addTransition("number", numbaValueState);

        openingBracketState.addTransition("chary", charyState);
        charyState.addTransition("=", charyAssignState);
        charyAssignState.addTransition("char", charyValueState);

        numbaValueState.addTransition("+", arithmeticOpState);
        numbaValueState.addTransition("-", arithmeticOpState);
        numbaValueState.addTransition("*", arithmeticOpState);
        numbaValueState.addTransition("/", arithmeticOpState);

        arithmeticOpState.addTransition("number", numbaValueState);

        grabEndState.addTransition("}", closingBracketState);
        shoutEndState.addTransition("}", closingBracketState);
        numbaValueState.addTransition("}", closingBracketState);
        charyValueState.addTransition("}", closingBracketState);

        // Create NFA
        Set<State> nfaStates = new HashSet<>(Arrays.asList(startState, openingBracketState, grabState, grabIdentifierState, grabEndState,
                shoutState, shoutIdentifierState, shoutEndState, numbaState, numbaAssignState, numbaValueState,
                charyState, charyAssignState, charyValueState, arithmeticOpState, closingBracketState));

        NFA nfa = new NFA(startState, nfaStates);
        nfa.displayTransitionTable();

        // Convert NFA to DFA
        System.out.println("\n🔹 Converting NFA to DFA...");
        NFAToDFAConverter converter = new NFAToDFAConverter();
        DFA dfa = converter.convert(nfa);
        dfa.displayTransitionTable();

        System.out.println("\n📌 Total NFA States: " + nfa.getStates().size());
        System.out.println("📌 Total DFA States: " + dfa.getTotalStates());
    }
}
