package net.semanticlab.lib.graph.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.google.common.primitives.Ints;
import lombok.extern.slf4j.Slf4j;



/**
 * Reads a network from a text file
 * 
 * @author albert
 *
 */

@Slf4j
public class NetworkReader {

    private NetworkReader() {}

    public static Network readNetwork(File f) {
        List<Integer> sourceNodes = new ArrayList<>();
        List<Integer> dstNodes = new ArrayList<>();
        try (Scanner scanner = new Scanner(f)) {
            while (scanner.hasNextInt()) {
                sourceNodes.add(scanner.nextInt());
                dstNodes.add(scanner.nextInt());
            }
        } catch (FileNotFoundException e) {
            log.error("Cannot open network file {}: {}", f, e);
        }
        return Network.getNetwork(sourceNodes.size(), Ints.toArray(sourceNodes),
                Ints.toArray(dstNodes), null);
    }
}
