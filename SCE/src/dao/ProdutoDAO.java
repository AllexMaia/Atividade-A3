/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import produtos.Produto;

/**
 *
 * @author Gabriel
 */
    
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author parto
 */
public class ProdutoDAO {
    public ArrayList <Produto> minhaLista = new ArrayList();
    private String user_check;
    private String pass_check;
    
    public ProdutoDAO(String user, String password) {
        this.user_check = user;
        this.pass_check = password;
    }
    

    public ArrayList <Produto> getMinhaLista () {
        minhaLista.clear();
        
        try {
            Statement stmt = this.getConexao ().createStatement();
            ResultSet res = stmt.executeQuery ("SELECT * FROM tb_produto");
            while (res.next()) {
                
                int Id = res.getInt("Id_produto");
                String Nome = res.getString("Nome_produto");
                double Preco = res.getDouble("Preco_produto");
                String Unidade = res.getString("Unidade_produto");
                int Quantidade_estoque = res.getInt("Quantidade_estoque");
                int Estoque_minimo = res.getInt("Estoque_minimo");
                int Estoque_maximo = res.getInt("Estoque_maximo");
                int id_categoria = res.getInt("id_categoria");
                String nome_categoria = res.getString("nome_categoria");
                String tamanho = res.getString("tamanho");
                String embalagem = res.getString("embalagem");
  
                
                Produto objeto = new Produto (Id, Nome, Preco, Unidade, Quantidade_estoque, Estoque_minimo, Estoque_maximo, id_categoria, nome_categoria, tamanho, embalagem);
                
                minhaLista.add(objeto);
                }
            stmt.close();
            } catch (SQLException ex) {
                System.out.println("Erro" + ex);
            }
            return minhaLista;
        }
        public int maiorID () {
            int maiorID = 0;
            
            try {
                Statement stmt = this.getConexao().createStatement();
                ResultSet res = stmt.executeQuery("SELECT MAX(id) id FROM tb_produto");
                res.next();
                maiorID = res.getInt("id");
                stmt.close();
            } catch (SQLException ex) {
                System.out.println("ERRO: " + ex);
            }
            return maiorID;
        }
            
            // Conexão com o Banco de Dados 
        
            public Connection getConexao(){
                
                Connection connection = null;
                try {
                    String driver = "com.mysql.cj.jdbc.Driver";
                    Class.forName(driver);
                    
                    String server = "localhost";              // Endereço do servidor MySQL (localhost = sua máquina)
                    String database = "estoque";            // Nome do banco
                    String url = "jdbc:mysql://" + server + ":3306/" + database + "?useTimezone=true&serverTimezone=UTC";
                    String user = user_check;
                    String password = pass_check;
                    
                    connection = DriverManager.getConnection (url, user, password);
                    
                    if (connection != null) {
                        System.out.println("Status: CONECTADO!");
                    } else {
                        System.out.println("Status: NÃO CONECTADO.");
                    }
                    return connection;
                    
                } catch (ClassNotFoundException e) {
                    System.out.println("O driver não foi encontrado. " + e.getMessage());
                    return null;
                } catch (SQLException e ){
                    System.out.println("Não foi possível conectar....");
                    return null;
                }
            }  
            public boolean insertProdutoBD (Produto objeto) {
                String sql = "INSERT INTO tb_produto (Id, Nome, Preco, Unidade, Quantidade_estoque, Estoque_minimo, Estoque_maximo) VALUES(?,?,?,?,?,?,?)";
                try {
                    PreparedStatement stmt = this.getConexao().prepareStatement(sql);
                    
                    stmt.setInt(1, objeto.getId());
                    stmt.setString(2, objeto.getNome());
                    stmt.setDouble(3, objeto.getPreco());
                    stmt.setString(4, objeto.getUnidade());
                    stmt.setInt(5, objeto.getQuantidade_estoque());
                    stmt.setInt(6, objeto.getEstoque_minimo());
                    stmt.setInt(7, objeto.getEstoque_maximo());
                    
                    stmt.execute();
                    stmt.close();
                    
                    return true;
                } catch (SQLException erro) {
                    System.out.println("Erro: " + erro);
                    throw new RuntimeException(erro);
                }
        
            }
            public boolean deleteProdutoBD (int id) {
                try {
                    Statement stmt = this.getConexao().createStatement();
                    stmt.executeUpdate("DELETE FROM tb_produto WHERE id = " + id);
                    stmt.close();
                } catch (SQLException erro) {
                    System.out.println("Erro: " + erro);
                }
                return true;
            }
            public boolean updateProdutoBD (Produto objeto) {
                String sql = "UPDATE tb_produto set Id = ?, Nome = ?, Preco = ?, Unidade = ?, Quantidade_estoque = ?, Estoque_minimo = ?, Estoque_maximo = ?";
                
                try {
                    PreparedStatement stmt = this.getConexao().prepareStatement(sql);
                    stmt.setInt(1, objeto.getId());
                    stmt.setString(2, objeto.getNome());
                    stmt.setDouble(3, objeto.getPreco());
                    stmt.setString(4, objeto.getUnidade());
                    stmt.setInt(5, objeto.getQuantidade_estoque());
                    stmt.setInt(6, objeto.getEstoque_minimo());
                    stmt.setInt(7, objeto.getEstoque_maximo());
                    
                    stmt.execute();
                    stmt.close();
                    
                    return true;
                    
                } catch (SQLException erro) {
                    System.out.println("Erro: " + erro);
                    throw new RuntimeException(erro);
                }
            } 

    /**
     *
     * @param id
     * @return
     */
    public Produto carregaProduto (int id) {
                Produto objeto = new Produto ();
                objeto.setId (id);
                
                try {
                    Statement stmt = this.getConexao().createStatement();
                    
                    ResultSet res = stmt.executeQuery("SELECT * FROM tb_categoria WHERE id = " + id);
                    res.next();
                    
                    objeto.setId (res.getInt("id"));
                    objeto.setNome (res.getString("Nome"));
                    objeto.setPreco (res.getDouble ("Preço"));
                    objeto.setUnidade (res.getString("Unidade"));
                    objeto.setQuantidade_estoque(res.getInt("Quantidade_estoque"));
                    objeto.setEstoque_minimo(res.getInt("Estoque_minimo"));
                    objeto.setEstoque_maximo(res.getInt("Estoque_maximo"));
                    
                    
                    stmt.close();
                } catch (SQLException erro) {
                    System.out.println("Erro:  + " + erro);
                }
                return objeto;
            }
    }
