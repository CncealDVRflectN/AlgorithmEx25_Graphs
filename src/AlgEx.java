import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Queue;

public class AlgEx {
    private static class WayPoint {
        int x;
        int y;

        public WayPoint() {
            x = 0;
            y = 0;
        }

        public WayPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public double calcDistance(WayPoint wayPoint) {
            return Math.sqrt(Math.pow(wayPoint.x - x, 2) + Math.pow(wayPoint.y - y, 2));
        }
    }

    private static boolean bfs(int[][] graph, int from, int to) {
        Queue<Integer> queue = new ArrayDeque<>();
        Integer curPoint;
        boolean[] isUsed = new boolean[to];
        way = new int[to];
        queue.add(from);
        while ((curPoint = queue.poll()) != null) {
            if (curPoint == to - 1) {
                isUsed[curPoint] = true;
                break;
            }
            if (!isUsed[curPoint]) {
                for (int i = 0; i < to; i++) {
                    if (!isUsed[i] && graph[curPoint][i] != 0) {
                        queue.add(i);
                        way[i] = curPoint;
                    }
                }
            }
            isUsed[curPoint] = true;
        }
        way[0] = -1;
        return isUsed[to - 1];
    }

    private static int[] way;

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        PrintWriter writer = new PrintWriter("output.txt");
        String[] buf = reader.readLine().split(" ");
        WayPoint[] wayPoints;
        WayPoint[] wantedPlaces;
        int[][] graph;
        int wayPointsNum = Integer.parseInt(buf[0]);
        int wantedPlacesNum = Integer.parseInt(buf[1]);
        int maxFlow = 0;
        int minFlow;
        double dist;
        wayPoints = new WayPoint[wayPointsNum];
        wantedPlaces = new WayPoint[wantedPlacesNum];
        graph = new int[wayPointsNum + wantedPlacesNum + 1][wayPointsNum + wantedPlacesNum + 1];
        for (int i = 0; i < wayPointsNum; i++) {
            buf = reader.readLine().split(" ");
            wayPoints[i] = new WayPoint(Integer.parseInt(buf[0]), Integer.parseInt(buf[1]));
        }
        for (int i = 0; i < wantedPlacesNum; i++) {
            buf = reader.readLine().split(" ");
            wantedPlaces[i] = new WayPoint(Integer.parseInt(buf[0]), Integer.parseInt(buf[1]));
            graph[wayPointsNum + i][wayPointsNum + wantedPlacesNum] = 1;
        }
        for (int i = 0; i < wayPointsNum - 1; i++) {
            graph[0][i + 1] = 1;
            dist = 2 * wayPoints[i].calcDistance(wayPoints[i + 1]);
            for (int j = 0; j < wantedPlacesNum; j++) {
                if (wayPoints[i].calcDistance(wantedPlaces[j]) + wantedPlaces[j].calcDistance(wayPoints[i + 1]) <= dist) {
                    graph[i + 1][j + wayPointsNum] = 1;
                }
            }
        }
        while (bfs(graph, 0, wayPointsNum + wantedPlacesNum + 1)) {
            minFlow = Integer.MAX_VALUE;
            for (int i = wayPointsNum + wantedPlacesNum; way[i] >= 0; i = way[i]) {
                minFlow = Math.min(minFlow, graph[way[i]][i]);
            }
            for (int i = wayPointsNum + wantedPlacesNum; way[i] >= 0; i = way[i]) {
                graph[way[i]][i] -= minFlow;
                graph[i][way[i]] += minFlow;
            }
            maxFlow += minFlow;
        }
        writer.print(wayPointsNum + maxFlow);
        writer.print(" ");
        writer.print(maxFlow);
        reader.close();
        writer.close();
    }
}
