package e.pmart.project;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Deikstra {
    private List<Rib> ribs;
    private int peaksCount, ribsCount;
    private int taskFrom, taskTo;

    private static class Rib {
        int from;
        int  to;
        int weight;

        private Rib(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    public Deikstra initialize(int peaksCount, int ribsCount, ArrayList<ArrayList<Integer>> ribs, int taskFrom, int taskTo) {
        this.peaksCount = peaksCount;
        this.ribsCount = ribsCount;

        this.ribs = new LinkedList<>();
        for (int i = 0; i < ribsCount; i++) {
            for (int j = 0; j < ribsCount; j++) {
                try {
                    if (ribs.get(i).get(j) != 0)
                        this.ribs.add(new Rib(i, j, ribs.get(i).get(j)));
                } catch (Exception e) {}
            }
        }
        this.taskFrom = taskFrom;
        this.taskTo = taskTo;

        return this;
    }

    public int main() throws Exception {
        if(ribsCount != 0) {
            List<Integer> usedPeaks = new LinkedList<>();
            usedPeaks.add(taskFrom);
            int currentPeak = taskFrom;
            int result = 0;

            //костыль на случай, если из заданного старта есть прямой
            //путь в заданный финиш, т.к. итоговый путь может оказаться
            //больше него
            int directLineWeight = 0;
            for(Rib r : ribs) {
                if(r.from == taskFrom && r.to == taskTo) {
                    directLineWeight = r.weight;
                }
            }

            while(currentPeak != taskTo && usedPeaks.size() != peaksCount) {
                List<Rib> currentRibsList = new LinkedList<>();

                //составляем список возможных для использования ребер
                for(Rib r : ribs) {
                    if (r.from == currentPeak && !usedPeaks.contains(r.to)) {
                        currentRibsList.add(r);
                    }
                }

                //если не найдено ни одного подходящего ребра
                if(currentRibsList.size() == 0) {
                    return directLineWeight;
                }
                ribs.removeAll(currentRibsList);

                //ищем в этом списке ребро, с использованием которого
                //удастся достичь наименьшего НА ДАННЫЙ МОМЕНТ веса
                int minWeight = Integer.MAX_VALUE;
                Rib ribToUse = null;
                for(Rib r : currentRibsList) {
                    if(r.weight + result < minWeight) {
                        minWeight = r.weight;
                        ribToUse = r;
                    }
                }
                currentRibsList.remove(ribToUse);
                ribs.addAll(currentRibsList);

                //меняем текущие значения для переменных-показателей статуса
                currentPeak = ribToUse.to;
                usedPeaks.add(ribToUse.to);
                result += ribToUse.weight;

                //выводит список вершин, обойденных на данный момент
				/*for(int r : usedPeaks) {
					System.out.print(r);
				}
				System.out.println();*/
            }

            //если нужного пути не существует, выводится -1
            if(currentPeak != taskTo &&  usedPeaks.size() == peaksCount){
                return directLineWeight;
            } else {
                if(directLineWeight != 0 && result > directLineWeight) {
                    return directLineWeight;
                } else {
                    return result;
                }
            }
        } else {
            throw new Exception("All wrong");
        }
    }
}
