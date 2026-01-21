package ru.kata.spring.boot_security.demo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ru.kata.spring.boot_security.demo.model.Role;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public void updateRole(Role role) {
        entityManager.merge(role);
    }

    @Override
    public void removeRole(Long id) {
        entityManager.createQuery("DELETE FROM Role r WHERE r.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public Role getRoleById(Long id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public List<Role> getAllRoles() {
        TypedQuery<Role> query = entityManager.createQuery("SELECT r FROM Role r", Role.class);
        return query.getResultList();
    }

    @Override
    public Role getRoleByName(String name) {
        TypedQuery<Role> query = entityManager.createQuery(
                "SELECT r FROM Role r WHERE r.name = :name",
                Role.class
        );
        query.setParameter("name", name);

        List<Role> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}
