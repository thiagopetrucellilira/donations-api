package com.donation.api.config;

import com.donation.api.entity.User;
import com.donation.api.entity.Donation;
import com.donation.api.entity.Match;
import com.donation.api.entity.enums.FoodCategory;
import com.donation.api.entity.enums.UserRole;
import com.donation.api.repository.UserRepository;
import com.donation.api.repository.DonationRepository;
import com.donation.api.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DonationRepository donationRepository;
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verificar se já existem dados no banco
        if (userRepository.count() > 0) {
            System.out.println("=== Dados já existem no banco, pulando inicialização ===");
            return;
        }
        
        System.out.println("=== Iniciando população do banco de dados ===");
        
        // Criar usuários
        createUsers();
        
        // Criar doações
        createDonations();
        
        // Criar matches
        createMatches();
        
        System.out.println("=== População do banco concluída com sucesso ===");
    }
    
    private void createUsers() {
        System.out.println("Criando usuários...");
        
        // Senha padrão para todos os usuários
        String defaultPassword = passwordEncoder.encode("123456");
        
        // DOADORES
        User joao = new User();
        joao.setName("João Silva Santos");
        joao.setEmail("joao.silva@email.com");
        joao.setPassword(defaultPassword);
        joao.setPhone("(11) 99876-5432");
        joao.setAddress("Rua das Flores, 123");
        joao.setCity("São Paulo");
        joao.setState("SP");
        joao.setZipCode("01234-567");
        joao.setBio("Supermercadista local que doa alimentos com prazo de validade próximo do vencimento mas ainda adequados ao consumo. Acredito que nenhum alimento deve ser desperdiçado!");
        joao.setRole(UserRole.DONOR);
        joao.setEnabled(true);
        joao.setCreatedAt(LocalDateTime.now());
        userRepository.save(joao);
        
        User maria = new User();
        maria.setName("Maria Fernanda Costa");
        maria.setEmail("maria.costa@email.com");
        maria.setPassword(defaultPassword);
        maria.setPhone("(21) 98765-4321");
        maria.setAddress("Av. Copacabana, 456");
        maria.setCity("Rio de Janeiro");
        maria.setState("RJ");
        maria.setZipCode("22070-000");
        maria.setBio("Professora aposentada e cozinheira amadora. Amo preparar e compartilhar refeições. Frequentemente doo marmitas prontas e alimentos excedentes das minhas receitas.");
        maria.setRole(UserRole.DONOR);
        maria.setEnabled(true);
        maria.setCreatedAt(LocalDateTime.now());
        userRepository.save(maria);
        
        User carlos = new User();
        carlos.setName("Carlos Eduardo Oliveira");
        carlos.setEmail("carlos.oliveira@email.com");
        carlos.setPassword(defaultPassword);
        carlos.setPhone("(31) 97654-3210");
        carlos.setAddress("Rua do Comércio, 789");
        carlos.setCity("Belo Horizonte");
        carlos.setState("MG");
        carlos.setZipCode("30120-000");
        carlos.setBio("Comerciante de hortifruti. Doo regularmente frutas, verduras e legumes frescos que não foram vendidos no dia.");
        carlos.setRole(UserRole.DONOR);
        carlos.setEnabled(true);
        carlos.setCreatedAt(LocalDateTime.now());
        userRepository.save(carlos);
        
        User ana = new User();
        ana.setName("Ana Paula Ribeiro");
        ana.setEmail("ana.ribeiro@email.com");
        ana.setPassword(defaultPassword);
        ana.setPhone("(47) 96543-2109");
        ana.setAddress("Rua Santa Catarina, 321");
        ana.setCity("Blumenau");
        ana.setState("SC");
        ana.setZipCode("89010-000");
        ana.setBio("Nutricionista voluntária. Participo de campanhas sociais e doo cestas básicas, alimentos funcionais e orientações nutricionais para famílias em vulnerabilidade.");
        ana.setRole(UserRole.DONOR);
        ana.setEnabled(true);
        ana.setCreatedAt(LocalDateTime.now());
        userRepository.save(ana);
        
        User roberto = new User();
        roberto.setName("Roberto Mendes Silva");
        roberto.setEmail("roberto.mendes@email.com");
        roberto.setPassword(defaultPassword);
        roberto.setPhone("(85) 95432-1098");
        roberto.setAddress("Av. Beira Mar, 654");
        roberto.setCity("Fortaleza");
        roberto.setState("CE");
        roberto.setZipCode("60165-000");
        roberto.setBio("Restauranteiro aposentado. Doo alimentos não perecíveis e conservas do antigo restaurante que ainda estão dentro da validade.");
        roberto.setRole(UserRole.DONOR);
        roberto.setEnabled(true);
        roberto.setCreatedAt(LocalDateTime.now());
        userRepository.save(roberto);
        
        // SOLICITANTES
        User lucia = new User();
        lucia.setName("Lúcia Maria Santos");
        lucia.setEmail("lucia.santos@email.com");
        lucia.setPassword(defaultPassword);
        lucia.setPhone("(11) 94321-0987");
        lucia.setAddress("Rua da Esperança, 147");
        lucia.setCity("São Paulo");
        lucia.setState("SP");
        lucia.setZipCode("08100-000");
        lucia.setBio("Mãe solteira de dois filhos. Trabalho como diarista e preciso de ajuda com alimentos básicos para minha família.");
        lucia.setRole(UserRole.REQUESTER);
        lucia.setEnabled(true);
        lucia.setCreatedAt(LocalDateTime.now());
        userRepository.save(lucia);
        
        User pedro = new User();
        pedro.setName("Pedro Henrique Alves");
        pedro.setEmail("pedro.alves@email.com");
        pedro.setPassword(defaultPassword);
        pedro.setPhone("(21) 93210-9876");
        pedro.setAddress("Rua Nova Esperança, 258");
        pedro.setCity("Rio de Janeiro");
        pedro.setState("RJ");
        pedro.setZipCode("21040-000");
        pedro.setBio("Estudante universitário em situação de vulnerabilidade. Preciso de alimentos não perecíveis e às vezes de refeições prontas.");
        pedro.setRole(UserRole.REQUESTER);
        pedro.setEnabled(true);
        pedro.setCreatedAt(LocalDateTime.now());
        userRepository.save(pedro);
        
        User sandra = new User();
        sandra.setName("Sandra Regina Lima");
        sandra.setEmail("sandra.lima@email.com");
        sandra.setPassword(defaultPassword);
        sandra.setPhone("(31) 92109-8765");
        sandra.setAddress("Rua do Progresso, 369");
        sandra.setCity("Belo Horizonte");
        sandra.setState("MG");
        sandra.setZipCode("31030-000");
        sandra.setBio("Cuidadora de idosos. Mãe de três filhos, preciso de alimentos básicos e laticínios para as crianças.");
        sandra.setRole(UserRole.REQUESTER);
        sandra.setEnabled(true);
        sandra.setCreatedAt(LocalDateTime.now());
        userRepository.save(sandra);
        
        User jose = new User();
        jose.setName("José Carlos Ferreira");
        jose.setEmail("jose.ferreira@email.com");
        jose.setPassword(defaultPassword);
        jose.setPhone("(47) 91098-7654");
        jose.setAddress("Rua da Solidariedade, 741");
        jose.setCity("Blumenau");
        jose.setState("SC");
        jose.setZipCode("89020-000");
        jose.setBio("Desempregado há 6 meses. Pai de família procurando oportunidades e precisando de cestas básicas para sustentar os filhos.");
        jose.setRole(UserRole.REQUESTER);
        jose.setEnabled(true);
        jose.setCreatedAt(LocalDateTime.now());
        userRepository.save(jose);
        
        User francisca = new User();
        francisca.setName("Francisca Silva Sousa");
        francisca.setEmail("francisca.sousa@email.com");
        francisca.setPassword(defaultPassword);
        francisca.setPhone("(85) 90987-6543");
        francisca.setAddress("Rua do Futuro, 852");
        francisca.setCity("Fortaleza");
        francisca.setState("CE");
        francisca.setZipCode("60180-000");
        francisca.setBio("Avó cuidando de 4 netos. Preciso de alimentos não perecíveis e leite para as crianças.");
        francisca.setRole(UserRole.REQUESTER);
        francisca.setEnabled(true);
        francisca.setCreatedAt(LocalDateTime.now());
        userRepository.save(francisca);
        
        System.out.println("Usuários criados: " + userRepository.count());
    }
    
    private void createDonations() {
        System.out.println("Criando doações...");
        
        // Buscar usuários doadores
        List<User> donors = userRepository.findAll().stream()
            .filter(user -> user.getRole() == UserRole.DONOR)
            .toList();
        
        if (donors.isEmpty()) {
            System.out.println("Nenhum doador encontrado!");
            return;
        }
        
        // Doações do João (primeiro doador)
        User joao = donors.get(0);
        
        Donation donation1 = new Donation();
        donation1.setTitle("Cesta Básica Completa");
        donation1.setDescription("Cesta completa com arroz 5kg, feijão 1kg, óleo 2L, macarrão 500g, açúcar 1kg, sal 1kg e farinha de trigo 1kg. Todos os produtos dentro do prazo de validade.");
        donation1.setCategory(FoodCategory.GRAOS_CEREAIS);
        donation1.setQuantity(5);
        donation1.setPerishable(false);
        donation1.setExpirationDate(LocalDate.now().plusMonths(6));
        donation1.setStorageInstructions("Manter em local seco e arejado, longe de umidade.");
        donation1.setLocation("Próximo ao Shopping Eldorado");
        donation1.setCity("São Paulo");
        donation1.setState("SP");
        donation1.setZipCode("01234-567");
        donation1.setStatus(Donation.DonationStatus.AVAILABLE);
        donation1.setPickupInstructions("Disponível para retirada de segunda a sexta, das 18h às 21h. Favor entrar em contato antes.");
        donation1.setExpiresAt(LocalDateTime.now().plusDays(30));
        donation1.setCreatedAt(LocalDateTime.now());
        donation1.setDonor(joao);
        donationRepository.save(donation1);
        
        Donation donation2 = new Donation();
        donation2.setTitle("Frutas e Legumes Frescos");
        donation2.setDescription("Caixas com bananas, maçãs, tomates e cenouras colhidos recentemente. Ideal para distribuição imediata.");
        donation2.setCategory(FoodCategory.HORTIFRUTI);
        donation2.setQuantity(3);
        donation2.setPerishable(true);
        donation2.setExpirationDate(LocalDate.now().plusDays(5));
        donation2.setStorageInstructions("Conservar em local fresco. Refrigerar após aberto.");
        donation2.setLocation("Região Central da Cidade");
        donation2.setCity("São Paulo");
        donation2.setState("SP");
        donation2.setZipCode("01234-567");
        donation2.setStatus(Donation.DonationStatus.AVAILABLE);
        donation2.setPickupInstructions("Retirada urgente — alimentos perecíveis. Combinar via WhatsApp.");
        donation2.setExpiresAt(LocalDateTime.now().plusDays(3));
        donation2.setCreatedAt(LocalDateTime.now());
        donation2.setDonor(joao);
        donationRepository.save(donation2);
        
        // Doações da Maria (segundo doador)
        if (donors.size() > 1) {
            User maria = donors.get(1);
            
            Donation donation3 = new Donation();
            donation3.setTitle("Leite Integral e Derivados");
            donation3.setDescription("Caixas de leite integral (1L cada), iogurte natural e queijo minas frescal. Produtos frescos de qualidade.");
            donation3.setCategory(FoodCategory.LATICINIOS);
            donation3.setQuantity(10);
            donation3.setPerishable(true);
            donation3.setExpirationDate(LocalDate.now().plusDays(10));
            donation3.setStorageInstructions("Manter refrigerado entre 2°C e 8°C. Consumir após aberto em até 3 dias.");
            donation3.setLocation("Copacabana - Próximo à praia");
            donation3.setCity("Rio de Janeiro");
            donation3.setState("RJ");
            donation3.setZipCode("22070-000");
            donation3.setStatus(Donation.DonationStatus.AVAILABLE);
            donation3.setPickupInstructions("Retirada preferencialmente pela manhã. Moro próximo à estação do metrô.");
            donation3.setExpiresAt(LocalDateTime.now().plusDays(7));
            donation3.setCreatedAt(LocalDateTime.now());
            donation3.setDonor(maria);
            donationRepository.save(donation3);
            
            Donation donation4 = new Donation();
            donation4.setTitle("Enlatados e Conservas Variados");
            donation4.setDescription("Sardinhas em lata, atum, milho em conserva, ervilhas, molho de tomate e extrato de tomate. Todos com validade longa.");
            donation4.setCategory(FoodCategory.ENLATADOS_CONSERVAS);
            donation4.setQuantity(20);
            donation4.setPerishable(false);
            donation4.setExpirationDate(LocalDate.now().plusYears(1));
            donation4.setStorageInstructions("Armazenar em local seco e fresco, longe de luz solar direta.");
            donation4.setLocation("Copacabana - Zona Sul");
            donation4.setCity("Rio de Janeiro");
            donation4.setState("RJ");
            donation4.setZipCode("22070-000");
            donation4.setStatus(Donation.DonationStatus.AVAILABLE);
            donation4.setPickupInstructions("Posso entregar em pontos de fácil acesso no Rio. Prefiro combinar por telefone.");
            donation4.setExpiresAt(LocalDateTime.now().plusDays(60));
            donation4.setCreatedAt(LocalDateTime.now());
            donation4.setDonor(maria);
            donationRepository.save(donation4);
        }
        
        // Mais doações dos outros doadores...
        if (donors.size() > 2) {
            User carlos = donors.get(2);
            
            Donation donation5 = new Donation();
            donation5.setTitle("Marmitas Prontas - Almoço Solidário");
            donation5.setDescription("Marmitas com arroz, feijão, frango grelhado e salada. Preparadas hoje cedo e acondicionadas adequadamente.");
            donation5.setCategory(FoodCategory.REFEICAO_PRONTA);
            donation5.setQuantity(8);
            donation5.setPerishable(true);
            donation5.setExpirationDate(LocalDate.now());
            donation5.setStorageInstructions("Manter refrigerado. Consumir no mesmo dia ou aquecer antes de consumir.");
            donation5.setLocation("Centro Comercial - Região do Mercado Central");
            donation5.setCity("Belo Horizonte");
            donation5.setState("MG");
            donation5.setZipCode("30120-000");
            donation5.setStatus(Donation.DonationStatus.AVAILABLE);
            donation5.setPickupInstructions("Retirada urgente até as 15h de hoje. Após esse horário as marmitas não estarão mais disponíveis.");
            donation5.setExpiresAt(LocalDateTime.now().plusHours(8));
            donation5.setCreatedAt(LocalDateTime.now());
            donation5.setDonor(carlos);
            donationRepository.save(donation5);
        }
        
        System.out.println("Doações criadas: " + donationRepository.count());
    }
    
    private void createMatches() {
        System.out.println("Criando matches...");
        
        // Buscar algumas doações e solicitantes para criar matches
        List<Donation> donations = donationRepository.findAll();
        List<User> requesters = userRepository.findAll().stream()
            .filter(user -> user.getRole() == UserRole.REQUESTER)
            .toList();
        
        if (donations.isEmpty() || requesters.isEmpty()) {
            System.out.println("Não há doações ou solicitantes suficientes para criar matches");
            return;
        }
        
        // Criar alguns matches de exemplo
        if (donations.size() > 0 && requesters.size() > 0) {
            Match match1 = new Match();
            match1.setMessage("Olá! Sou mãe solteira de dois filhos e estamos precisando muito de alimentos. Posso buscar no horário que for melhor para você. Muito obrigada pela oportunidade!");
            match1.setStatus(Match.MatchStatus.PENDING);
            match1.setRequestedAt(LocalDateTime.now());
            match1.setCreatedAt(LocalDateTime.now());
            match1.setDonation(donations.get(0));
            match1.setRequester(requesters.get(0));
            matchRepository.save(match1);
        }
        
        if (donations.size() > 1 && requesters.size() > 1) {
            Match match2 = new Match();
            match2.setMessage("Oi! Sou estudante com dificuldades financeiras e os alimentos seriam de grande ajuda. Posso buscar no final de semana. Desde já agradeço!");
            match2.setStatus(Match.MatchStatus.APPROVED);
            match2.setRequestedAt(LocalDateTime.now().minusDays(2));
            match2.setCreatedAt(LocalDateTime.now().minusDays(2));
            match2.setDonation(donations.get(1));
            match2.setRequester(requesters.get(1));
            matchRepository.save(match2);
        }
        
        System.out.println("Matches criados: " + matchRepository.count());
    }
}
