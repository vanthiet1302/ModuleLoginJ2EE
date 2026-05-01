package dev.nlu.moduleloginj2ee.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "is_active", nullable = false)
        @Builder.Default
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserCredential credentials;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<OAuthAccount> oauthAccounts;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<UserPermissionGrant> userPermissionGrants;

    public boolean hasPermission(String resource, String action) {
        if (isSuperAdmin()) {
            return true;
        }

        if (roles == null || roles.isEmpty() || resource == null || action == null) {
            return hasTemporaryPermission(resource, action);
        }

        boolean grantedByRole = roles.stream()
                .filter(Objects::nonNull)
                .map(Role::getPermissions)
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .filter(Objects::nonNull)
                .anyMatch(p -> resource.equals(p.getResource())
                        && action.equals(p.getAction()));

        return grantedByRole || hasTemporaryPermission(resource, action);
    }

    public Set<Permission> getAllPermissions() {
        if ((roles == null || roles.isEmpty())
                && (userPermissionGrants == null || userPermissionGrants.isEmpty())) {
            return Collections.emptySet();
        }

        Map<String, Permission> permissionMap = new LinkedHashMap<>();

        if (roles != null) {
            roles.stream()
                .filter(Objects::nonNull)
                .map(Role::getPermissions)
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .filter(Objects::nonNull)
                .forEach(permission -> addPermission(permissionMap, permission));
        }

        if (userPermissionGrants != null) {
            userPermissionGrants.stream()
                    .filter(Objects::nonNull)
                    .filter(UserPermissionGrant::isActive)
                    .map(UserPermissionGrant::getPermission)
                    .filter(Objects::nonNull)
                    .forEach(permission -> addPermission(permissionMap, permission));
        }

        return permissionMap.values().stream()
                .sorted(Comparator.comparing(Permission::getResource, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Permission::getAction, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public boolean isSuperAdmin() {
        return hasRole("SUPER_ADMIN");
    }

    private boolean hasTemporaryPermission(String resource, String action) {
        if (userPermissionGrants == null || userPermissionGrants.isEmpty()
                || resource == null || action == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        return userPermissionGrants.stream()
                .filter(Objects::nonNull)
                .filter(grant -> grant.isActiveAt(now))
                .map(UserPermissionGrant::getPermission)
                .filter(Objects::nonNull)
                .anyMatch(permission -> resource.equals(permission.getResource())
                        && action.equals(permission.getAction()));
    }

    private boolean hasRole(String roleName) {
        if (roles == null || roles.isEmpty() || roleName == null || roleName.isBlank()) {
            return false;
        }

        return roles.stream()
                .filter(Objects::nonNull)
                .map(Role::getName)
                .filter(Objects::nonNull)
                .anyMatch(name -> roleName.equalsIgnoreCase(name.trim()));
    }

    private void addPermission(Map<String, Permission> permissionMap, Permission permission) {
        if (permission.getResource() == null || permission.getAction() == null) {
            return;
        }

        String key = permission.getResource().trim().toUpperCase() + ":" + permission.getAction().trim().toUpperCase();
        permissionMap.putIfAbsent(key, permission);
    }

    public boolean isEmailVerified() {
        if (credentials != null && credentials.isEmailVerified()) {
            return true;
        }

//        if (oauthAccounts != null) {
//            return oauthAccounts.stream()
//                    .filter(Objects::nonNull)
//                    .anyMatch(OAuthAccount::isEmailVerified);
//        }

        return false;
    }
}
