import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class Matches {
    private static final String FIRST_TEAM_NAME = "Team_1";
    private static final String SECOND_TEAM_NAME = "Team_2";
    private static final String DELIMITER = ":";
    private static final int POINTS_FOR_WIN = 3;
    private static final int POINTS_FOR_DRAW = 1;

    public static void main(String[] args) {
        List<String> competitions = List.of("3:1", "2:2", "0:1", "4:2", "3:a", "3- 12");

        Map<String, Integer> results = new HashMap<>();
        results.put(FIRST_TEAM_NAME, 0);
        results.put(SECOND_TEAM_NAME, 0);

        List<String> excludedResults = competitions.stream()
                .filter(e -> !e.matches("\\d*:\\d*"))
                .collect(Collectors.toList());

        log.info("Next results were excluded as they are not match declared format: {}", excludedResults);
        // Next results were excluded as they are not match declared format: [3:a, 3- 12]

        competitions.stream()
                .filter(e -> !excludedResults.contains(e))
                .map(e -> Pair.of(Integer.parseInt(e.split(DELIMITER)[0]), Integer.parseInt(e.split(DELIMITER)[1])))
                .forEach(p -> {
                    if (p.getLeft() > p.getRight()) {
                        results.compute(FIRST_TEAM_NAME, (k, v) -> Objects.requireNonNull(v) + POINTS_FOR_WIN);
                    } else if (p.getLeft() < p.getRight()) {
                        results.compute(SECOND_TEAM_NAME, (k, v) -> Objects.requireNonNull(v) + POINTS_FOR_WIN);
                    } else {
                        results.compute(FIRST_TEAM_NAME, (k, v) -> Objects.requireNonNull(v) + POINTS_FOR_DRAW);
                        results.compute(SECOND_TEAM_NAME, (k, v) -> Objects.requireNonNull(v) + POINTS_FOR_DRAW);
                    }
                });

        log.info("Number of points for each team - {}: {}, {}: {}", FIRST_TEAM_NAME,
                results.get(FIRST_TEAM_NAME), SECOND_TEAM_NAME, results.get(SECOND_TEAM_NAME));
        // Number of points for each team - Team_1: 7, Team_2: 4
    }
}
