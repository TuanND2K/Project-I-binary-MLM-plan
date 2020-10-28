public class Distributor {
    private static int IDSetter = 0;
    private int ID;
    private String name;
    private Distributor leftLeg;
    private Distributor rightLeg;
    private Distributor sponsor;
    private Distributor parent;
    private double commission;
    private int numberOfProducts = 0;

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

    public Distributor getSponsor() {
        return sponsor;
    }

    public void setSponsor(Distributor sponsor) {
        this.sponsor = sponsor;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }

    public Distributor getParent() {
        return parent;
    }

    public void setParent(Distributor parent) {
        this.parent = parent;
    }
    public Distributor() {

    }

    public Distributor(String name) {
        IDSetter++;
        ID = IDSetter;
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
        if(find(d1, d2.getID()) != null) return 1;
        if(find(d2, d1.getID()) != null) return -1;
        return 0;
    }
}
