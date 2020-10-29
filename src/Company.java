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

    public void deleteDistributor(Distributor d) {
        memberList.remove(d);
        Distributor toPromote = Distributor.distributorToPromote(d);
        Distributor notPromote = (d.getLeftLeg() == toPromote) ? d.getRightLeg() : d.getLeftLeg();
        Distributor parent = d.getParent();
        if(parent != null) {
            if(parent.getRightLeg() == d) {
                parent.setRightLeg(toPromote);
            } else parent.setLeftLeg(toPromote);
        }
        if(toPromote != null) reorder(toPromote, notPromote);
    }

    private void reorder(Distributor d, Distributor e) {
        if(e == null) return;
        if(d.getLeftLeg() == null) d.setLeftLeg(e);
        else if(d.getRightLeg() == null) d.setRightLeg(e);
        else {
            if(d.getRightLeg().getCommission() > d.getLeftLeg().getCommission()) {
                Distributor old_left = d.getLeftLeg();
                d.setLeftLeg(e);
                reorder(e, old_left);
            } else {
                Distributor old_right = d.getRightLeg();
                d.setRightLeg(e);
                reorder(e, old_right);
            }
        }
    }




}
