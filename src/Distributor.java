public class Distributor {
    private static int IDSetter = 0;
    private int ID;
    private String name;
    private Distributor leftLeg;
    private Distributor rightLeg;
    private Distributor sponsor;
    private Distributor parent;
    private double commission;
    private int productsSold = 0;

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

    public int getProductsSold() {
        return productsSold;
    }

    public void setProductsSold(int productsSold) {
        this.productsSold = productsSold;
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


    /**
     * tra ve so nha phan phoi cap duoi cua nha phan phoi dang xet
     */

    public int numberOfInferiors() {
        int count = 0;
        if(leftLeg != null) count += 1 + leftLeg.numberOfInferiors();
        if(rightLeg != null) count += 1 + rightLeg.numberOfInferiors();
        return count;
    }


    /**
     * tim nha phan phoi trong cay nhi phan biet cap tren va ID
     * @param superior doi tuong nha phan phoi cap tren
     * @param ID ID cua nha phan phoi can tim
     */
    public static Distributor find(Distributor superior, int ID) {
        if(superior == null) return null;
        //System.out.println("superior name " + superior.getName());
        if(superior.getID() == ID) return superior;
        Distributor found;
        found = find(superior.getLeftLeg(), ID);
        if(found != null) return found;
        found = find(superior.getRightLeg(), ID);
        return found;
    }


    /**
     * quan he giua 2 nha phan phoi d1 va d2
     * tra ve: 1 - d1 la cap tren cua d2
     *         -1 - d1 la cap duoi cua d2
     *         0 - neu khong co lien he giua d1 va d2
    */
    public static int relation(Distributor d1, Distributor d2) {
        if(find(d1, d2.getID()) != null) return 1;
        if(find(d2, d1.getID()) != null) return -1;
        return 0;
    }

    public boolean isFullOfLeg() {
        return getLeftLeg() != null && getRightLeg() != null;
    }

    /**
     * nha phan phoi thay the vi tri cua nha phan phoi d, trong truong hop xoa d khoi cay
     */
    public static Distributor distributorToPromote(Distributor d) {
        Distributor distributorToPromote = null;
        if(d.isFullOfLeg()) {
            if(d.getRightLeg().getCommission() > d.getLeftLeg().getCommission()) {
                distributorToPromote = d.getRightLeg();
            }
            else if(d.getRightLeg().getCommission() == d.getLeftLeg().getCommission()) {
                if(d.getRightLeg().productsSold > d.getLeftLeg().productsSold) {
                    distributorToPromote = d.getRightLeg();
                } else distributorToPromote = d.getLeftLeg();
            }
            else distributorToPromote = d.getLeftLeg();
        } else {
            if(d.getLeftLeg() == null && d.getRightLeg() != null) distributorToPromote = d.getRightLeg();
            if(d.getRightLeg() == null && d.getLeftLeg() != null) distributorToPromote = d.getLeftLeg();
        }
        return distributorToPromote;
    }


}
