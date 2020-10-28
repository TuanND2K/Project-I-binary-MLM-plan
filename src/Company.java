import java.util.ArrayList;

public class Company {

    private Distributor boss;
    private String name;
    private ArrayList<Distributor> memberList;

    public Distributor getBoss() {
        return boss;
    }

    public Company() {

    }
    public Company(Distributor boss, String name) {
        this.boss = boss;
        this.name = name;
        memberList = new ArrayList<>();
        memberList.add(boss);
    }

    public ArrayList<Distributor> getMemberList() {
        return memberList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean inCompany(Distributor d) {
        //System.out.println(d.getName());
        return Distributor.relation(boss, d) == 1;
    }

    public void addDistributor(Distributor newMember, Distributor sponsor) {
        if(!inCompany(sponsor)) return;
        if(!memberList.contains(newMember)) memberList.add(newMember);
        if(newMember.getSponsor() == null) newMember.setSponsor(sponsor);
        if(sponsor.getLeftLeg() == null) {
            sponsor.setLeftLeg(newMember);
            newMember.setParent(sponsor);
        }
        else if(sponsor.getRightLeg() == null) {
            sponsor.setRightLeg((newMember));
            newMember.setParent(sponsor);
        }
        else {
            if(sponsor.getLeftLeg().numberOfInferiors() > sponsor.getRightLeg().numberOfInferiors())
                addDistributor(newMember, sponsor.getRightLeg());
            else {
                addDistributor(newMember, sponsor.getLeftLeg());
            }
        }
    }

    public Distributor find(int ID) {
        return Distributor.find(boss, ID);
    }

    /*public boolean deleteDistributor(Distributor d) {
        if(!inCompany(d)) return false;
        Distributor parent = d.getParent();
        if(parent == null) boss = d;
        if(d.getRightLeg() != null && d.getLeftLeg() != null) {
            
        }
    }*/

}
