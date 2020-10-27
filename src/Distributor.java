public class Distributor {
    public static final double commissionRate = 0.1;
    private static int numOfDistributor = 0;
    private int ID;
    private String name;
    private Distributor leftLeg;
    private Distributor rightLeg;
    private Distributor superior;
    private int commission;
    private int numberOfProducts;

    public static int getNumOfDistributor() {
        return numOfDistributor;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Distributor getLeftLeg() {
        return leftLeg;
    }

    public void setLeftLeg(Distributor leftLeg) {
        this.leftLeg = leftLeg;
    }

    public Distributor getRightLeg() {
        return rightLeg;
    }

    public void setRightLeg(Distributor rightLeg) {
        this.rightLeg = rightLeg;
    }

    public Distributor getSuperior() {
        return superior;
    }

    public void setSuperior(Distributor superior) {
        this.superior = superior;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }
    public Distributor() {

    }

    public Distributor(String name) {
        numOfDistributor++;
        ID = numOfDistributor;
        this.name = name;
    }

    public int numberOfInferiors() {
        int count = 0;
        if(leftLeg != null) count += 1 + leftLeg.numberOfInferiors();
        if(rightLeg != null) count += 1 + rightLeg.numberOfInferiors();
        return count;
    }

    public static Distributor find(Distributor superior, int ID) {
        if(superior == null) return null;
        //System.out.println("superior name " + superior.getName());
        if(superior.getID() == ID) return superior;
        Distributor found = null;
        found = find(superior.getLeftLeg(), ID);
        if(found != null) return found;
        found = find(superior.getRightLeg(), ID);
        return found;
    }

    // relation between d1 and d2
    // return 1 if d1 if superior of d2, -1 if d1 is inferior of d2, 0 otherwise
    public static int relation(Distributor d1, Distributor d2) {
        //System.out.println("ralation between " + d1.name + " and " + d2.name);
        if(d1.getID() <= d2.getID()) {
            if(find(d1, d2.getID()) != null) return 1;
        } else {
            if(find(d2, d1.getID()) != null) return -1;
        }
        return 0;
    }
}
