package com.consultoria.imagem.util;

import com.consultoria.imagem.model.Cliente;
import com.consultoria.imagem.model.Loja;
import com.consultoria.imagem.model.Peca;
import com.consultoria.imagem.model.ListaDeCompras;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:consultoria.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");

                // Create tables
                createTables(conn);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        String sqlLojas = "CREATE TABLE IF NOT EXISTS lojas (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    nome TEXT NOT NULL,\n" +
                "    endereco TEXT,\n" +
                "    contato TEXT\n" +
                ");";

        String sqlClientes = "CREATE TABLE IF NOT EXISTS clientes (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    nome TEXT NOT NULL,\n" +
                "    contato TEXT,\n" +
                "    observacoes TEXT\n" +
                ");";

        String sqlPecas = "CREATE TABLE IF NOT EXISTS pecas (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    nome TEXT NOT NULL,\n" +
                "    descricao TEXT,\n" +
                "    fotoPath TEXT,\n" +
                "    loja_id INTEGER,\n" +
                "    valor REAL,\n" +
                "    observacoes TEXT,\n" +
                "    FOREIGN KEY (loja_id) REFERENCES lojas(id) ON DELETE CASCADE\n" +
                ");";

        String sqlListasDeCompras = "CREATE TABLE IF NOT EXISTS listas_de_compras (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    cliente_id INTEGER NOT NULL,\n" +
                "    nome TEXT NOT NULL,\n" +
                "    dataCriacao TEXT NOT NULL,\n" +
                "    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE\n" +
                ");";


        String sqlListaPecas = "CREATE TABLE IF NOT EXISTS lista_pecas (\n" +
                "    lista_id INTEGER NOT NULL,\n" +
                "    peca_id INTEGER NOT NULL,\n" +
                "    PRIMARY KEY (lista_id, peca_id),\n" +
                "    FOREIGN KEY (lista_id) REFERENCES listas_de_compras(id) ON DELETE CASCADE,\n" +
                "    FOREIGN KEY (peca_id) REFERENCES pecas(id) ON DELETE CASCADE\n" +
                ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sqlLojas);
            stmt.execute(sqlClientes);
            stmt.execute(sqlPecas);
            stmt.execute(sqlListasDeCompras);
            stmt.execute(sqlListaPecas);
            System.out.println("Tables created or already exist.");
        }
    }

    // --- Loja CRUD Operations ---
    public static void addLoja(Loja loja) {
        String sql = "INSERT INTO lojas(nome, endereco, contato) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, loja.getNome());
            pstmt.setString(2, loja.getEndereco());
            pstmt.setString(3, loja.getContato());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    loja.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Loja> getAllLojas() {
        List<Loja> lojas = new ArrayList<>();
        String sql = "SELECT id, nome, endereco, contato FROM lojas";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lojas.add(new Loja(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getString("contato")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lojas;
    }

    public static Loja getLojaById(int id) {
        String sql = "SELECT id, nome, endereco, contato FROM lojas WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Loja(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getString("contato"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void updateLoja(Loja loja) {
        String sql = "UPDATE lojas SET nome = ?, endereco = ?, contato = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loja.getNome());
            pstmt.setString(2, loja.getEndereco());
            pstmt.setString(3, loja.getContato());
            pstmt.setInt(4, loja.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteLoja(int id) {
        String sql = "DELETE FROM lojas WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // --- Cliente CRUD Operations ---
    public static void addCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes(nome, contato, observacoes) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getContato());
            pstmt.setString(3, cliente.getObservacoes());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Cliente> getAllClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, nome, contato, observacoes FROM clientes";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clientes.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("contato"),
                        rs.getString("observacoes")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return clientes;
    }

    public static Cliente getClienteById(int id) {
        String sql = "SELECT id, nome, contato, observacoes FROM clientes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("contato"),
                        rs.getString("observacoes"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void updateCliente(Cliente cliente) {
        String sql = "UPDATE clientes SET nome = ?, contato = ?, observacoes = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getContato());
            pstmt.setString(3, cliente.getObservacoes());
            pstmt.setInt(4, cliente.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteCliente(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // --- Peca CRUD Operations ---
    public static void addPeca(Peca peca) {
        String sql = "INSERT INTO pecas(nome, descricao, fotoPath, loja_id, valor, observacoes) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, peca.getNome());
            pstmt.setString(2, peca.getDescricao());
            pstmt.setString(3, peca.getFotoPath());
            pstmt.setInt(4, peca.getLoja().getId());
            pstmt.setDouble(5, peca.getValor());
            pstmt.setString(6, peca.getObservacoes());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    peca.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Peca> getAllPecas() {
        List<Peca> pecas = new ArrayList<>();
        String sql = "SELECT p.id, p.nome, p.descricao, p.fotoPath, p.loja_id, p.valor, p.observacoes, l.nome as loja_nome, l.endereco as loja_endereco, l.contato as loja_contato FROM pecas p JOIN lojas l ON p.loja_id = l.id";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Loja loja = new Loja(
                        rs.getInt("loja_id"),
                        rs.getString("loja_nome"),
                        rs.getString("loja_endereco"),
                        rs.getString("loja_contato"));
                pecas.add(new Peca(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getString("fotoPath"),
                        loja,
                        rs.getDouble("valor"),
                        rs.getString("observacoes")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return pecas;
    }

    public static Peca getPecaById(int id) {
        String sql = "SELECT p.id, p.nome, p.descricao, p.fotoPath, p.loja_id, p.valor, p.observacoes, l.nome as loja_nome, l.endereco as loja_endereco, l.contato as loja_contato FROM pecas p JOIN lojas l ON p.loja_id = l.id WHERE p.id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Loja loja = new Loja(
                        rs.getInt("loja_id"),
                        rs.getString("loja_nome"),
                        rs.getString("loja_endereco"),
                        rs.getString("loja_contato"));
                return new Peca(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getString("fotoPath"),
                        loja,
                        rs.getDouble("valor"),
                        rs.getString("observacoes"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void updatePeca(Peca peca) {
        String sql = "UPDATE pecas SET nome = ?, descricao = ?, fotoPath = ?, loja_id = ?, valor = ?, observacoes = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, peca.getNome());
            pstmt.setString(2, peca.getDescricao());
            pstmt.setString(3, peca.getFotoPath());
            pstmt.setInt(4, peca.getLoja().getId());
            pstmt.setDouble(5, peca.getValor());
            pstmt.setString(6, peca.getObservacoes());
            pstmt.setInt(7, peca.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deletePeca(int id) {
        String sql = "DELETE FROM pecas WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // --- ListaDeCompras CRUD Operations ---
    public static void addListaDeCompras(ListaDeCompras lista) {
        String sql = "INSERT INTO listas_de_compras(cliente_id, nome, dataCriacao) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, lista.getCliente().getId());
            pstmt.setString(2, lista.getNome()); // Adicionado nome da lista
            pstmt.setString(3, lista.getDataCriacao().toString());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    lista.setId(rs.getInt(1));
                }
            }

            // Add pecas to lista_pecas table
            for (Peca peca : lista.getPecas()) {
                addPecaToLista(lista.getId(), peca.getId());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<ListaDeCompras> getAllListasDeCompras() {
        List<ListaDeCompras> listas = new ArrayList<>();
        String sql = "SELECT l.id as lista_id, l.nome as lista_nome, l.dataCriacao, c.id as cliente_id, c.nome as cliente_nome, c.contato as cliente_contato, c.observacoes as cliente_observacoes FROM listas_de_compras l JOIN clientes c ON l.cliente_id = c.id";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("cliente_id"),
                        rs.getString("cliente_nome"),
                        rs.getString("cliente_contato"),
                        rs.getString("cliente_observacoes"));
                List<Peca> pecasDaLista = getPecasByListaId(rs.getInt("lista_id"));
                listas.add(new ListaDeCompras(
                        rs.getInt("lista_id"),
                        rs.getString("lista_nome"), // Adicionado nome da lista
                        cliente,
                        LocalDate.parse(rs.getString("dataCriacao")),
                        pecasDaLista));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listas;
    }

    public static List<ListaDeCompras> getListasByCliente(int clienteId) {
        List<ListaDeCompras> listas = new ArrayList<>();
        String sql = "SELECT l.id as lista_id, l.nome as lista_nome, l.dataCriacao, c.id as cliente_id, c.nome as cliente_nome, c.contato as cliente_contato, c.observacoes as cliente_observacoes FROM listas_de_compras l JOIN clientes c ON l.cliente_id = c.id WHERE l.cliente_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("cliente_id"),
                        rs.getString("cliente_nome"),
                        rs.getString("cliente_contato"),
                        rs.getString("cliente_observacoes"));
                List<Peca> pecasDaLista = getPecasByListaId(rs.getInt("lista_id"));
                listas.add(new ListaDeCompras(
                        rs.getInt("lista_id"),
                        rs.getString("lista_nome"),
                        cliente,
                        LocalDate.parse(rs.getString("dataCriacao")),
                        pecasDaLista));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listas;
    }

    public static ListaDeCompras getListaDeComprasById(int id) {
        String sql = "SELECT l.id as lista_id, l.nome as lista_nome, l.dataCriacao, c.id as cliente_id, c.nome as cliente_nome, c.contato as cliente_contato, c.observacoes as cliente_observacoes FROM listas_de_compras l JOIN clientes c ON l.cliente_id = c.id WHERE l.id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("cliente_id"),
                        rs.getString("cliente_nome"),
                        rs.getString("cliente_contato"),
                        rs.getString("cliente_observacoes"));
                List<Peca> pecasDaLista = getPecasByListaId(rs.getInt("lista_id"));
                return new ListaDeCompras(
                        rs.getInt("lista_id"),
                        rs.getString("lista_nome"),
                        cliente,
                        LocalDate.parse(rs.getString("dataCriacao")),
                        pecasDaLista);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void updateListaDeCompras(ListaDeCompras lista) {
        String sql = "UPDATE listas_de_compras SET nome = ?, cliente_id = ?, dataCriacao = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lista.getNome());
            pstmt.setInt(2, lista.getCliente().getId());
            pstmt.setString(3, lista.getDataCriacao().toString());
            pstmt.setInt(4, lista.getId());
            pstmt.executeUpdate();

            // Remove all existing pecas for this list
            deletePecasFromLista(lista.getId());

            // Add current pecas to lista_pecas table
            for (Peca peca : lista.getPecas()) {
                addPecaToLista(lista.getId(), peca.getId());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteListaDeCompras(int id) {
        String sql = "DELETE FROM listas_de_compras WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            // ON DELETE CASCADE na tabela lista_pecas deve cuidar da exclusão das peças associadas
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void addPecaToLista(int listaId, int pecaId) {
        String sql = "INSERT INTO lista_pecas(lista_id, peca_id) VALUES(?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, listaId);
            pstmt.setInt(2, pecaId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deletePecasFromLista(int listaId) {
        String sql = "DELETE FROM lista_pecas WHERE lista_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, listaId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Peca> getPecasByListaId(int listaId) {
        List<Peca> pecas = new ArrayList<>();
        String sql = "SELECT lp.peca_id FROM lista_pecas lp WHERE lp.lista_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, listaId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pecas.add(DatabaseManager.getPecaById(rs.getInt("peca_id")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return pecas;
    }

    public static void updateListaPecas(int listaId, List<Peca> novasPecas) {
        // Primeiro, remove todas as peças existentes para esta lista
        deletePecasFromLista(listaId);

        // Em seguida, adiciona as novas peças
        for (Peca peca : novasPecas) {
            addPecaToLista(listaId, peca.getId());
        }
    }

}
