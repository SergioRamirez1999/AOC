package me.sergioramirez.days.day20;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Puzzle {
    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day20\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);
    private static final Map<Module, List<Module>> network = init_network();
    private static final List<Module> modules = network.keySet().stream().toList();

    private static Map<Module, List<Module>> init_network() {
        Map<Module, List<Module>> network = new HashMap<>();

        for(String line: input) {
            String[] lineArr = line.split("->");
            String nameLine = lineArr[0].trim();
            network.put(buildModule(nameLine), new ArrayList<>());
        }

        for(String line: input) {
            String[] inputNames = line.split("->")[1].replaceAll(" ", "").split(",");
            List<Module> inputs = Arrays.stream(inputNames).map(n -> network.keySet().stream().filter(m -> m.getName().equals(n)).findFirst().orElseGet(() -> new Module(n, null, null, null))).toList();
            network.put(buildModule(line.split("->")[0].trim()), inputs);
        }

        for(Module mod: network.keySet()) {
            List<Module> inputs = network.entrySet().stream().filter(kv -> kv.getValue().contains(mod)).map(Map.Entry::getKey).toList();
            mod.setMemory_pulse(inputs.stream().collect(Collectors.toMap(Function.identity(), k -> "low")));
        }

        return network;
    }

    public static Module buildModule(String nameLine) {
        String name;
        String type;
        if(nameLine.equals("broadcaster")) {
            name = nameLine;
            type = "broadcaster";
        } else {
            if(nameLine.charAt(0) == '%') { //FlipFlop
                type = "Flip-flop";
            } else { //Conjuntion
                type = "Conjuntion";
            }
            name = nameLine.substring(1);
        }
        return new Module(name, type, "off", null);
    }

    public static long total_pulses() {
        long low_pulses = 0;
        long high_pulses = 0;
        Module broadcaster = modules.stream().filter(m -> m.getName().equals("broadcaster")).findFirst().get();
        List<Module> broadcaster_destination = network.get(broadcaster);
        List<Pulse> pulses = broadcaster_destination.stream().map(m -> new Pulse(broadcaster, m, "low")).toList();
        ArrayDeque<Pulse> deque = new ArrayDeque<>();
        for(int i = 0; i < 1000; i++) {
            low_pulses++;
            deque.addAll(pulses);

            while(!deque.isEmpty()) {

                Pulse pulse = deque.poll();
                Module source = pulse.getSource();
                Module nextInput = pulse.getDestination();
                String pulse_type = pulse.getType();

                if(pulse_type.equals("low"))
                    low_pulses++;
                else
                    high_pulses++;

                if(!network.containsKey(nextInput))
                    continue;

                if(nextInput.getType().equals("Flip-flop")) {
                    if(pulse_type.equals("low")) {
                        nextInput.setStatus(nextInput.getStatus().equals("on") ? "off" : "on");
                        String pulse_type_destination = nextInput.getStatus().equals("on") ? "high" : "low";
                        deque.addAll(network.get(nextInput).stream().map(d -> new Pulse(nextInput, d, pulse_type_destination)).toList());
                    }
                }else {
                    nextInput.getMemory_pulse().put(source, pulse_type);
                    String pulse_type_destination = nextInput.getMemory_pulse().values().stream().allMatch(p -> p.equals("high")) ? "low" : "high";
                    deque.addAll(network.get(nextInput).stream().map(d -> new Pulse(nextInput, d, pulse_type_destination)).toList());

                }

            }
        }

        return low_pulses * high_pulses;
    }


    public static long total_pulses_rx() {
        long presses = 0;
        long pulses_low_to_rx = 0;
        Module broadcaster = modules.stream().filter(m -> m.getName().equals("broadcaster")).findFirst().get();
        List<Module> broadcaster_destination = network.get(broadcaster);
        List<Pulse> source_pulses = broadcaster_destination.stream().map(m -> new Pulse(broadcaster, m, "low")).toList();

        Module source_rx = network.entrySet().stream().filter(kv -> kv.getValue().stream().anyMatch(m -> m.getName().equals("rx"))).map(Map.Entry::getKey).findFirst().get();
        List<Module> inputs_source_rx = network.entrySet().stream().filter(kv -> kv.getValue().stream().anyMatch(m -> m.equals(source_rx))).map(Map.Entry::getKey).toList();

        Map<Module, Long> cycles_length = new HashMap<>();

        while (pulses_low_to_rx == 0) {
            presses++;

            ArrayDeque<Pulse> deque = new ArrayDeque<>(source_pulses);

            while (!deque.isEmpty()) {

                Pulse pulse = deque.poll();
                Module source = pulse.getSource();
                Module nextInput = pulse.getDestination();
                String pulse_type = pulse.getType();


                if (!network.containsKey(nextInput))
                    continue;

                if(nextInput.equals(source_rx) && pulse_type.equals("high")) {
                    if(!cycles_length.containsKey(source))
                        cycles_length.put(source, presses);

                    if(cycles_length.size() == inputs_source_rx.size()) {
                        pulses_low_to_rx = cycles_length.values().stream().findFirst().get();
                        for(Long cycles: cycles_length.values()) {
                            pulses_low_to_rx = me.sergioramirez.days.day08.Puzzle.mcm(cycles, pulses_low_to_rx);
                        }
                    }
                }


                if (nextInput.getType().equals("Flip-flop")) {
                    if (pulse_type.equals("low")) {
                        nextInput.setStatus(nextInput.getStatus().equals("on") ? "off" : "on");
                        String pulse_type_destination = nextInput.getStatus().equals("on") ? "high" : "low";
                        deque.addAll(network.get(nextInput).stream().map(d -> new Pulse(nextInput, d, pulse_type_destination)).toList());
                    }
                } else {
                    nextInput.getMemory_pulse().put(source, pulse_type);
                    String pulse_type_destination = nextInput.getMemory_pulse().values().stream().allMatch(p -> p.equals("high")) ? "low" : "high";
                    deque.addAll(network.get(nextInput).stream().map(d -> new Pulse(nextInput, d, pulse_type_destination)).toList());

                }

            }
        }
        return pulses_low_to_rx;
    }



    public static void main(String[] args) {
        long result_A = total_pulses();
        System.out.println(result_A);
        long result_B = total_pulses_rx();
        System.out.println(result_B);
    }
}
