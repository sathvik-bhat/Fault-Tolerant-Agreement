import common.*;
import java.util.*;

public class Machine_0009 extends Machine {
    Machine_0009() {
        id = ++count;
        location = new Location(0, 0);
        direction = 'r';
        machineList = new ArrayList<Machine_0009>();
        stepSize = 0;
        phase = 0;
        r1leftCount = 0;
        r2leftCount = 0;
        r1rightCount = 0;
        r2rightCount = 0;
        numFaulty = 0;
        round0Decision = -1;
    }

    public void run() {
        int n = 100;
        while (n-- > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // ROUND 0
            synchronized (Machine_0009.machineList) {
                if (isLeader()) {
                    int min = 0;
                    int max = 1;
                    int decision = (int) (Math.random() * (max - min + 1) + min);
                    sendMessage((int) getId(), Machine_0009.phase, 0, decision);
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // ROUND 1
            synchronized (Machine_0009.machineList) {
                sendMessage((int) getId(), Machine_0009.phase, 1, getr0Decision());

            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // ROUND 2
            synchronized (Machine_0009.machineList) {
                sendMessage((int) getId(), Machine_0009.phase, 2, getr1Decision());
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (Machine_0009.machineList) {
                boolean confirmation = confirmr2Decision();
                if (confirmation) {
                    move();
                } else {
                    System.out.println("Error");
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // RESET
            synchronized (Machine_0009.machineList) {
                reset();
            }

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(int sourceId, int phaseNum, int roundNum, int decision) {
        synchronized (Machine_0009.machineList) {
            if (roundNum == 0) {
                for (Machine_0009 machine : machineList) {
                    int min = 0;
                    int max = 1;
                    int rand = (int) (Math.random() * (max - min + 1) + min);
                    if (isCorrect())
                        machine.setr0Decision(decision);
                    else {
                        machine.setr0Decision(rand);
                    }
                }
            }

            else if (roundNum == 1) {
                int min = 0;
                int max = 1;
                int rand = (int) (Math.random() * (max - min + 1) + min);
                int send = (int) (Math.random() * (max - min + 1) + min);
                for (Machine_0009 machine : machineList) {
                    if (isCorrect())
                        machine.reciever1Decision(decision);
                    else {
                        if (send == 1) {
                            machine.reciever1Decision(rand);
                        }
                    }
                }
            }

            if (roundNum == 2) {
                for (Machine_0009 machine : machineList) {
                    machine.reciever2Decision(decision);
                }
            }
        }
    }

    protected void move() {
        if (direction == 'u') {
            if (getr2Decision() == 0) {
                location.setLoc(location.getX() - stepSize, location.getY());
                direction = 'l';
            } else if (getr2Decision() == 1) {
                location.setLoc(location.getX() + stepSize, location.getY());
                direction = 'r';
            }
        } else if (direction == 'd') {
            if (getr2Decision() == 0) {
                location.setLoc(location.getX() + stepSize, location.getY());
                direction = 'r';
            } else if (getr2Decision() == 1) {
                location.setLoc(location.getX() - stepSize, location.getY());
                direction = 'l';
            }
        } else if (direction == 'r') {
            if (getr2Decision() == 0) {
                location.setLoc(location.getX(), location.getY() + stepSize);
                direction = 'u';
            } else if (getr2Decision() == 1) {
                location.setLoc(location.getX(), location.getY() - stepSize);
                direction = 'd';
            }
        } else if (direction == 'l') {
            if (getr2Decision() == 0) {
                location.setLoc(location.getX(), location.getY() - stepSize);
                direction = 'd';
            } else if (getr2Decision() == 1) {
                location.setLoc(location.getX(), location.getY() + stepSize);
                direction = 'u';
            }
        }
    }

    public void reset() {
        machineList = new ArrayList<Machine_0009>();
        phase++;
        round0Decision = -1;
        r1leftCount = 0;
        r2leftCount = 0;
        r1rightCount = 0;
        r2rightCount = 0;
        numFaulty = 0;
    }

    public void setr0Decision(int decision) {
        round0Decision = decision;
    }

    public int getr0Decision() {
        return round0Decision;
    }

    public void reciever1Decision(int decision) {
        if (decision == 0) {
            r1leftCount++;
        } else if (decision == 1) {
            r1rightCount++;
        }
    }

    public int getr1Decision() {
        if (r1leftCount > r1rightCount) {
            return 0;
        } else if (r1leftCount < r1rightCount) {
            return 1;
        }
        return -1;
    }

    public void reciever2Decision(int decision) {
        if (decision == 0) {
            r2leftCount++;
        } else if (decision == 1) {
            r2rightCount++;
        }
    }

    public int getr2Decision() {
        if (r2leftCount >= 2 * numFaulty + 1) {
            return 0;
        }
        if (r2rightCount >= 2 * numFaulty + 1) {
            return 1;
        }
        return -1;
    }

    public boolean confirmr2Decision() {
        if (r2leftCount >= 2 * numFaulty + 1 || r2rightCount >= 2 * numFaulty + 1) {
            return true;
        }
        return false;
    }


    public void setState(boolean isCorrect1) {
        isCorrect = isCorrect1;
        if (isCorrect == false) {
            numFaulty++;
        }
        machineList.add(this);
    }

    public void setStepSize(int stepSize1) {
        stepSize = stepSize1;
    }

    public void setLeader() {
        isLeader = true;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public String name() {
        return ("Machine " + id);
    }

    public Location getPosition() {
        return location;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    private static int count = 0;
    private int stepSize;
    private Character direction;
    private int id;
    Location location;
    private boolean isCorrect;
    private boolean isLeader;
    private static int phase;
    private int r1leftCount;
    private int r2leftCount;
    private int r1rightCount;
    private int r2rightCount;
    private int round0Decision;
    private static int numFaulty;
    private static ArrayList<Machine_0009> machineList;
}