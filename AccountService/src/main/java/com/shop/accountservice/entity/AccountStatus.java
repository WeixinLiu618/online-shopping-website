package com.shop.accountservice.entity;

/**
 * ✅1. ACTIVE
 * Meaning: Account is fully functional.
 * Allowed actions:
 * Login.
 * Place orders.
 * Update profile.
 * Usage:
 * When a user successfully registers, default status = ACTIVE.
 * In business logic, check if (account.getStatus() == AccountStatus.ACTIVE) before allowing order placement.
 * ✅ 2. INACTIVE
 * Meaning: Temporarily disabled (e.g., user requested deactivation, security lock).
 * Allowed actions:
 * Cannot log in.
 * Cannot place orders.
 * Can only contact support to reactivate.
 * Usage:
 * At login time:
 * java
 * Copy
 * Edit
 * if (account.getStatus() == AccountStatus.INACTIVE) {
 * throw new IllegalStateException("Account is inactive. Contact support.");
 * }
 * Add endpoint for admins: PATCH /accounts/{id}/activate or deactivate.
 * ✅ 3. DELETED
 * Meaning: Account is logically deleted (soft delete).
 * Why soft delete:
 * Keeps historical order data.
 * GDPR compliance (hide personal data, but keep orders).
 * Allowed actions:
 * No login.
 * No updates.
 * Usage:
 * When user chooses "Delete account", mark status as DELETED instead of removing from DB:
 * java
 * Copy
 * Edit
 * account.setStatus(AccountStatus.DELETED);
 * accountRepository.save(account);
 * Exclude DELETED accounts from normal queries (add condition in repository or service).
 */
public enum AccountStatus {
    ACTIVE,
    INACTIVE,
    DELETED
}
