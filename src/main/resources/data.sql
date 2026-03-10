-- ========================================
-- LIMPEZA INICIAL
-- ========================================

-- Limpar dados existentes na ordem correta (foreign keys)
DELETE FROM matches;
DELETE FROM donations;
DELETE FROM users;

-- Resetar contadores de ID para garantir IDs previsíveis
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE donations AUTO_INCREMENT = 1;
ALTER TABLE matches AUTO_INCREMENT = 1;

-- ========================================
-- DADOS SERÃO INSERIDOS VIA API
-- ========================================
-- Este arquivo apenas limpa o banco
-- Os usuários serão criados via endpoint de registro
-- Isso garante que o hash BCrypt seja gerado corretamente

