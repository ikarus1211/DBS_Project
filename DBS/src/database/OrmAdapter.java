package database;

import javax.persistence.*;

public class OrmAdapter {
    private final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("DBS_DaMA_Project");

    public void addCharacter(String fname,int hours, int money, String race, String cClass,
                             int xp, boolean guildL, int owner_id) {
        // The EntityManager class allows operations such as create, read, update, delete
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        // Used to issue transactions on the EntityManager
        EntityTransaction et = null;

        try {
            // Get transaction and start
            et = em.getTransaction();
            et.begin();

            // Create and set values for new customer
            ORMgamecharacter character = new ORMgamecharacter();

            character.setcName(fname);
            character.setHours_played(hours);
            character.setMoney(money);
            character.setChar_race(race);
            character.setChar_class(cClass);
            character.setXp(xp);
            character.setGuild_leader(guildL);
            character.setGuild_id(null);
            character.setOwner_id(getPlayer(owner_id));

            // Save the customer object
            em.persist(character);
            et.commit();
        } catch (Exception ex) {
            // If there is an exception rollback changes
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            // Close EntityManager
            em.close();
        }
    }

    public int getMoney(int id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        String query = "SELECT c FROM game_character c WHERE c.id = :charID";

        TypedQuery<ORMgamecharacter> tq = em.createQuery(query, ORMgamecharacter.class);
        tq.setParameter("charID", id);

        ORMgamecharacter gameChar = null;
        try {
            // Get matching customer object and output
            gameChar = tq.getSingleResult();
        }
        catch(NoResultException ex) {
            ex.printStackTrace();
            return -1;
        }
        finally {
            em.close();

        }
        return gameChar.getMoney();

    }
    public ORMplayer getPlayer(int id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        // the lowercase c refers to the object
        // :custID is a parameterized query thats value is set below
        String query = "SELECT p FROM player p WHERE p.id = :pID";

        // Issue the query and get a matching Customer
        TypedQuery<ORMplayer> tq = em.createQuery(query, ORMplayer.class);
        tq.setParameter("pID", id);

        ORMplayer player = null;
        try {
            // Get matching customer object and output
            player = tq.getSingleResult();
            System.out.println(player.getpName() + " " + player.getpPassword());
        }
        catch(NoResultException ex) {
            ex.printStackTrace();
        }
        finally {
            em.close();
        }
        return player;
    }
}
