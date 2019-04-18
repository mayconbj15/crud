package user;

import crud.Arquivo;

import entidades.Compra;

import util.IO;

public class CrudCompra extends CrudAbstract<Compra>
{
	public CrudCompra(Arquivo<Compra> database)
	{
		super(database);
	}

	public void listarCategorias()
	{
		IO.println("Categorias disponíveis:\n");
		listar();
	}
	
	public int menuInclusao(Compra compra)
	{	
		int success = -1;
		
		if(compra != null) {
			success = inserir(compra);
		}
			
		
		return success;
	}

	/**
	 * Altera o valor total de uma categoria caso o valor unitário de uma compra seja alterado.
	 * {@code cod} é responsável por indicar qual dos campos da
	 * categoria deseja-se alterar.
	 * 
	 * <p></p>
	 * 
	 * <table style="border: 1px solid black; text-align: center;">
	 * 
	 * 	<tr>
	 * 		<th>{@code cod}</th> <th>campo</th>
	 * 	</tr>
	 * 
	 * 	<tr>
	 * 		<td>1</td> <td>nome</td>
	 * 	</tr>
	 * 
	 * </table>
	 * 
	 * @param id Id da entidade a ser alterada.
	 * @param cod Código do campo a ser alterado.
	 * 
	 * @return {@code false} se alguma coisa falhar na alteração.
	 * Caso contrário, retorna {@code true}.
	 */
	
	public Compra alterarValorTotal(int id)
	{
		// procurar a categoria desejada na base de dados
		Compra compra=  database.readObject(id);
		
		if (compra != null) { // checa se a categoria foi encontrada	
			// [EM DESENVOLVIMENTO]
		}
		
		else
		{
			IO.println("\nCategoria não encontrada.\n");
		}

		return compra;
	}//end alterar()
	

	public void menuConsulta()
	{
		int id = IO.readint("Digite o id da compra ser consultada: ");
		
		consultar(id);
	}
	
	/**
	 * Lista todos os produtos que estão na categoria com o id informado.
	 * 
	 * @param id Id da categoria a se listar os produtos.
	 */
	
	/*public void listarProdutos(int id) 
	{				
		if(Main.crudProduto != null){
			int [] lista = Main.indiceComposto.listarDadosComAChave(id);
			int tamanho = lista.length;
			
			for(int y = 0; y < tamanho; y++)
			{
				Main.crudProduto.consultar(lista[y]);
			}//end for
		}
		
	}//end listarProdutos()*/
	
	public void menuListar()
	{
		listar();		
	}

	public void menuExclusao(){
		listarCategorias();
		int cod = -1; //codigo de selecao
		int id = IO.readint("Digite o id da compra a ser removida: ");

		//testar antes se o id existe
		if (database.idIsValid(id)){
			IO.println("Realmente deseja excluir a compra " + database.readObject(id).getId() + " ?");
			IO.println("Digite:");
			IO.println("1 Sim");
			IO.println("2 Não");
			IO.println("");
			cod = IO.readint("Opção: ");
			
			if (cod == 1){
				excluir(id);
			}
			else if(cod == 2) {
				IO.println("Operação cancelada");
			}
		}
		else{
			IO.println("Id inválido!");
		}
		
	}//end menuExclusao
}
