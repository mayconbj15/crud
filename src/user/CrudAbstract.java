package user;

import crud.Arquivo;
import entidades.Entidade;
import serializaveis.SerializavelAbstract;
import util.IO;

public abstract class CrudAbstract<TIPO_ENTIDADE extends SerializavelAbstract & Entidade>
{
	protected Arquivo<TIPO_ENTIDADE> database;
	
	public CrudAbstract(Arquivo<TIPO_ENTIDADE> database)
	{
		this.database = database;
	}
	
	/**
	 * Insere uma entidade na base de dados.
	 * 
	 * @param entidade Entidade a ser inserida.
	 * 
	 * @return Novo id se tudo der certo;
	 *  caso contrário -1.
	 */
	
	public int inserir(TIPO_ENTIDADE entidade)
	{
		int newID = database.writeObject(entidade);
		
		if ( newID > -1 )
		{
			IO.println("\nEntidade cadastrada com sucesso! :D\n");
		}
		
		else
		{
			IO.println("\nFalha no cadastramento da entidade.\n");
		}
		
		return newID;
	}
	
	/**
	 * Altera a entidade com o id informado substituindo-a
	 * pela nova entidade informada.
	 * 
	 * @param id Id da entidade a ser alterada.
	 * @param entity Nova entidade a ser colocada no lugar.
	 * 
	 * @return se tudo der certo, a entidade antes de ser
	 * alterada; {@code null} caso contrário.
	 */
	
	public TIPO_ENTIDADE alterar(int id, TIPO_ENTIDADE entity)
	{
		TIPO_ENTIDADE deletedEntity = database.changeObject(id, entity);
		
		if (deletedEntity != null)
		{
			IO.println("\nEntidade alterada com sucesso! :D\n");
		}
		
		else
		{
			IO.println("\nFalha na alteração da entidade.\n");
		}
		
		return deletedEntity;
	}
	
	/**
	 * Exclui a entidade com o id informado.
	 * 
	 * @param id Id da entidade a ser excluída.
	 * 
	 * @return a entidade excluída se tudo der certo;
	 * {@code null} caso contrário.
	 */
	
	public TIPO_ENTIDADE excluir(int id)
	{
		TIPO_ENTIDADE deletedEntity = database.deleteObject(id);
		
		if (deletedEntity != null)
		{
			IO.println("Entidade excluída com sucesso.");
		}
		
		else
		{
			IO.println("Falha na exclusão da entidade.");
		}
		
		return deletedEntity;
	}
	
	/**
	 * Consulta a entidade com o id informado.
	 * 
	 * @param id Id da entidade a ser consultada.
	 * @param paraOUsuario Mostra apenas informações
	 * úteis para o usuário.
	 * 
	 * @return {@code true} se tudo der certo;
	 * {@code false} caso contrário.
	 */
	
	private boolean consultar(int id, boolean paraOUsuario)
	{
		boolean success = false;
		
		if (database.entityExists(id)) // checa se a entidade existe
		{
			TIPO_ENTIDADE entity = database.readObject(id);
			
			if(entity != null)
			{
				IO.println( "\n" + ( paraOUsuario ? entity.printToUser() : entity.print() ));
				
				success = true;
			}
			else
			{
				IO.println("Entidade inexistente");
			}				
		}
		
		else
		{
			IO.println("Id inválido!");
		}
		
		return success;
	}
	
	/**
	 * Consulta a entidade com o id informado.
	 * 
	 * @param id Id da entidade a ser consultada.
	 * 
	 * @return {@code true} se tudo der certo;
	 * {@code false} caso contrário.
	 */
	
	public boolean consultar(int id)
	{
		return consultar(id, false);
	}
	
	/**
	 * Consulta a entidade com o id informado mostrando
	 * apenas informações relevantes para o usuário.
	 * 
	 * @param id Id da entidade a ser consultada.
	 * 
	 * @return {@code true} se tudo der certo;
	 * {@code false} caso contrário.
	 */
	
	public boolean consultarParaOUsuario(int id)
	{
		return consultar(id, true);
	}
	
	/**
	 * Lista todas as entidades ativas da base de dados.
	 */
	
	public void listar()
	{
		database.list().forEach(
			(entidade) -> { IO.println(entidade.print() + "\n"); }
		);
	}
}
