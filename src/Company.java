public class Company {
    private Distributor boss;
    private int levels;

    public Distributor getBoss() {
        return boss;
    }

    public Company(Distributor boss) {
        this.boss = boss;
    }

    public boolean inCompany(Distributor d) {
        //System.out.println(d.getName());
        if(Distributor.relation(boss, d) == 1) return true;
        else return false;
    }

    public void addDistributor(Distributor newMember, Distributor superior) {
        //System.out.println("new member name: " + newMember.getName());
        //System.out.println("superior name: " + superior.getName());
        if(!inCompany(superior)) return;
        //System.out.println("in company");
        if(superior.getLeftLeg() == null) superior.setLeftLeg(newMember);
        else if(superior.getRightLeg() == null) superior.setRightLeg((newMember));
        else {
            if(superior.getLeftLeg().numberOfInferiors() > superior.getRightLeg().numberOfInferiors())
                addDistributor(newMember, superior.getRightLeg());
            else {
                addDistributor(newMember, superior.getLeftLeg());
            }
        }
    }

    public Distributor find(int ID) {
        return Distributor.find(boss, ID);
    }

}
