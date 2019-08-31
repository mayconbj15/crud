/* See src/crud/hash/LICENSE for license information */

package crud.hash;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiFunction;

import serializaveis.SerializavelAbstract;

/**
 * Classe que gerencia um bucket específico.
 * 
 * @author Axell Brendow ( https://github.com/axell-brendow )
 *
 * @param <TIPO_DAS_CHAVES> Classe da chave.
 * @param <TIPO_DOS_DADOS> Classe do dado.
 */

public class Bucket<TIPO_DAS_CHAVES extends SerializavelAbstract, TIPO_DOS_DADOS extends SerializavelAbstract> extends SerializavelAbstract
{
	// o cabeçalho do bucket é apenas a profundidade local até o momento
	public static final int DESLOCAMENTO_CABECALHO = Byte.BYTES;
	
	public static final byte PADRAO_PROFUNDIDADE_LOCAL = 0;
	protected static final byte PADRAO_NUMERO_DE_REGISTROS_POR_BUCKET = 10;
	
	protected byte profundidadeLocal;
	protected int numeroDeRegistrosPorBucket;
	protected RegistroDoIndice<TIPO_DAS_CHAVES, TIPO_DOS_DADOS> registroDoIndice;
	byte[] bucket;
	String toStr;
	
	/**
	 * Cria um objeto que gerencia um bucket da hash dinâmica.
	 * 
	 * @param bucket Arranjo de bytes do bucket.
	 * @param profundidadeLocal Profundidade local inicial.
	 * @param numeroDeRegistrosPorBucket Numero de registros por bucket caso o arquivo
	 * não tenha sido criado ainda.
	 * @param quantidadeMaximaDeBytesParaAChave Tamanho máximo que a chave pode gastar.
	 * @param quantidadeMaximaDeBytesParaODado Tamanho máximo que o dado pode gastar.
	 * @param construtorDaChave Construtor da chave. É necessário que a chave tenha um
	 * construtor sem parâmetros.
	 * @param construtorDoDado Construtor do dado. É necessário que o dado tenha um
	 * construtor sem parâmetros.
	 */
	
	private Bucket(
		byte[] bucket,
		byte profundidadeLocal,
		int numeroDeRegistrosPorBucket,
		short quantidadeMaximaDeBytesParaAChave,
		short quantidadeMaximaDeBytesParaODado,
		Constructor<TIPO_DAS_CHAVES> construtorDaChave,
		Constructor<TIPO_DOS_DADOS> construtorDoDado)
	{
		this.bucket = bucket;
		this.profundidadeLocal =
			( profundidadeLocal < 1 ? PADRAO_PROFUNDIDADE_LOCAL : profundidadeLocal );
		this.numeroDeRegistrosPorBucket =
			( numeroDeRegistrosPorBucket < 1 ?
				PADRAO_NUMERO_DE_REGISTROS_POR_BUCKET : numeroDeRegistrosPorBucket );
		
		this.registroDoIndice =
			new RegistroDoIndice<>(
				RegistroDoIndice.REGISTRO_DESATIVADO, null, null,
				quantidadeMaximaDeBytesParaAChave,
				quantidadeMaximaDeBytesParaODado,
				construtorDaChave,
				construtorDoDado
			);
	}
	
	/**
	 * Cria um objeto que gerencia um bucket da hash dinâmica.
	 * 
	 * @param profundidadeLocal Profundidade local inicial.
	 * @param numeroDeRegistrosPorBucket Numero de registros por bucket caso o arquivo
	 * não tenha sido criado ainda.
	 * @param quantidadeMaximaDeBytesParaAChave Tamanho máximo que a chave pode gastar.
	 * @param quantidadeMaximaDeBytesParaODado Tamanho máximo que o dado pode gastar.
	 * @param construtorDaChave Construtor da chave. É necessário que a chave tenha um
	 * construtor sem parâmetros.
	 * @param construtorDoDado Construtor do dado. É necessário que o dado tenha um
	 * construtor sem parâmetros.
	 */
	
	public Bucket(
		byte profundidadeLocal,
		int numeroDeRegistrosPorBucket,
		short quantidadeMaximaDeBytesParaAChave,
		short quantidadeMaximaDeBytesParaODado,
		Constructor<TIPO_DAS_CHAVES> construtorDaChave,
		Constructor<TIPO_DOS_DADOS> construtorDoDado)
	{
		this
		(
			null,
			profundidadeLocal,
			numeroDeRegistrosPorBucket,
			quantidadeMaximaDeBytesParaAChave,
			quantidadeMaximaDeBytesParaODado,
			construtorDaChave,
			construtorDoDado
		);
		
		iniciarBucket();
	}
	
	/**
	 * Cria um objeto que gerencia um bucket da hash dinâmica. A profundidade local
	 * inicial para o bucket será {@link #PROFUNDIDADE_LOCAL_PADRAO}.
	 * 
	 * @param numeroDeRegistrosPorBucket Numero de registros por bucket caso o arquivo
	 * não tenha sido criado ainda.
	 * @param quantidadeMaximaDeBytesParaAChave Tamanho máximo que a chave pode gastar.
	 * @param quantidadeMaximaDeBytesParaODado Tamanho máximo que o dado pode gastar.
	 * @param construtorDaChave Construtor da chave. É necessário que a chave tenha um
	 * construtor sem parâmetros.
	 * @param construtorDoDado Construtor do dado. É necessário que o dado tenha um
	 * construtor sem parâmetros.
	 */
	
	public Bucket(
		int numeroDeRegistrosPorBucket,
		short quantidadeMaximaDeBytesParaAChave,
		short quantidadeMaximaDeBytesParaODado,
		Constructor<TIPO_DAS_CHAVES> construtorDaChave,
		Constructor<TIPO_DOS_DADOS> construtorDoDado)
	{
		this(
			PADRAO_PROFUNDIDADE_LOCAL,
			numeroDeRegistrosPorBucket,
			quantidadeMaximaDeBytesParaAChave,
			quantidadeMaximaDeBytesParaODado,
			construtorDaChave,
			construtorDoDado);
	}
	
	/**
	 * Define a profundidade local do bucket.
	 * 
	 * @param profundidadeLocal Profundidade local do bucket.
	 * 
	 * @return {@code profundidadeLocal}.
	 */
	
	public byte atribuirProfundidadeLocal(byte profundidadeLocal)
	{
		if (profundidadeLocal >= 0)
		{
			this.profundidadeLocal = profundidadeLocal;
			bucket[0] = profundidadeLocal;
		}
		
		return profundidadeLocal;
	}
	
	/**
	 * Calcula o tamanho em bytes que um bucket gasta.
	 * 
	 * @return o tamanho em bytes que um bucket gasta.
	 */
	
	private int obterTamanhoDeUmBucket()
	{
		return DESLOCAMENTO_CABECALHO + // tamanho, em bytes, do cabeçalho (metadados)
			numeroDeRegistrosPorBucket * registroDoIndice.obterTamanhoMaximoEmBytes();
	}

	@Override
	public int obterTamanhoMaximoEmBytes()
	{
		return obterTamanhoDeUmBucket();
	}
	
	/**
	 * Aloca o espaço máximo que um bucket pode gastar em bytes e já inicia os
	 * registros do bucket como desativados, além de colocar também a profundidade
	 * local do bucket no primeiro byte.
	 * 
	 * <p>Ilustração da estrutura de um bucket:</p>
	 * 
	 * <b>[ profundidade local (byte), registros... ({@link RegistroDoIndice}) ]</b>
	 */
	
	private void iniciarBucket()
	{
		bucket = new byte[obterTamanhoDeUmBucket()];
		bucket[0] = profundidadeLocal;
		
		int tamanhoDeUmRegistro = registroDoIndice.obterTamanhoMaximoEmBytes();
		int deslocamento = DESLOCAMENTO_CABECALHO;
		
		// desativa todos os registros no bucket
		for (int i = 0; i < numeroDeRegistrosPorBucket; i++)
		{
			bucket[deslocamento] = RegistroDoIndice.REGISTRO_DESATIVADO;
			
			deslocamento += tamanhoDeUmRegistro;
		}
	}
	
	/**
	 * Imaginando que os registros do bucket estão num arranjo,
	 * retorna o registro no indice informado.
	 * 
	 * @param indiceDoBucket Indice do registro a ser obtido.
	 * 
	 * @return o registro no indice informado.
	 */
	
	protected RegistroDoIndice<TIPO_DAS_CHAVES, TIPO_DOS_DADOS>
		obterRegistro(int indiceDoBucket)
	{
		registroDoIndice.lerBytes(bucket,
			DESLOCAMENTO_CABECALHO + indiceDoBucket * registroDoIndice.obterTamanhoMaximoEmBytes());
		
		return registroDoIndice;
	}
	
	/**
	 * Percorre todos os registros do bucket aplicando um método
	 * em cada um deles. Esse método deve retornar um valor inteiro
	 * que indica se o procedimento deve parar ou não. O retorno
	 * 0 indica que o processo deve continuar, qualquer retorno
	 * diferente termina o processo. O primeiro parâmetro que o
	 * método recebe é o registro em questão. O segundo parâmetro é
	 * o deslocamento em relação ao início do arranjo {@code bucket}
	 * em que o registro está.
	 * 
	 * @param metodo Método a ser aplicado em cada registro.
	 * 
	 * @return 0 se o {@code metodo} sempre retornar 0. Caso contrário,
	 * retorna o que o {@code metodo} retornar.
	 */
	
	public int percorrerRegistros(
		BiFunction<RegistroDoIndice<TIPO_DAS_CHAVES, TIPO_DOS_DADOS>, Integer, Integer> metodo)
	{
		int condicao = 0;
		
		if (metodo != null)
		{
			int deslocamento = DESLOCAMENTO_CABECALHO;
			int tamanhoDeUmRegistro = registroDoIndice.obterTamanhoMaximoEmBytes();
			
			for (int i = 0; condicao == 0 && i < numeroDeRegistrosPorBucket; i++)
			{
				registroDoIndice.lerBytes(bucket, deslocamento);
				
				condicao = metodo.apply(registroDoIndice, deslocamento);
				
				deslocamento += tamanhoDeUmRegistro;
			}
		}
		
		return condicao;
	}
	
	/**
	 * Procura um registro no bucket com a chave e o dado informados.
	 * 
	 * @param chave Chave a ser procurada.
	 * @param dado Dado que corresponde à chave.
	 * 
	 * @return {@code 0} se o registro não for encontrado;
	 * caso contrário, o deslocamento em relação ao início
	 * do bucket em que o registro está.
	 */
	
	protected int pesquisarEnderecoDaChaveEDoDado(TIPO_DAS_CHAVES chave, TIPO_DOS_DADOS dado)
	{
		return
		percorrerRegistros(
			(registro, deslocamento) ->
			{
				int status = 0; // indica que o processo deve continuar
				
				if (registro.lapide == RegistroDoIndice.REGISTRO_ATIVADO &&
					registro.chave.toString().equals(chave.toString()) &&
					registro.dado.toString().equals(dado.toString()))
				{
					status = deslocamento; // término com êxito, registro já existe
				}
				
				return status;
			}
		);
	}
	
	/**
	 * Procura um registro no bucket com a chave informada.
	 * 
	 * @param chave Chave a ser procurada.
	 * 
	 * @return {@code 0} se o registro não for encontrado;
	 * caso contrário, o deslocamento em relação ao início
	 * do bucket em que o registro está.
	 */
	
	protected int pesquisarEnderecoDaChave(TIPO_DAS_CHAVES chave)
	{
		return
		percorrerRegistros(
			(registro, deslocamento) ->
			{
				int status = 0; // indica que o processo deve continuar
				
				if (registro.lapide == RegistroDoIndice.REGISTRO_ATIVADO &&
					registro.chave.toString().equals(chave.toString()))
				{
					status = deslocamento; // término com êxito, registro já existe
				}
				
				return status;
			}
		);
	}
	
	/**
	 * Procura um registro no bucket com o dado informado.
	 * 
	 * @param dado Dado a ser procurado.
	 * 
	 * @return {@code 0} se o registro não for encontrado;
	 * caso contrário, o deslocamento em relação ao início
	 * do bucket em que o registro está.
	 */
	
	protected int pesquisarEnderecoDoDado(TIPO_DOS_DADOS dado)
	{
		return
		percorrerRegistros(
			(registro, deslocamento) ->
			{
				int status = 0; // indica que o processo deve continuar
				
				if (registro.lapide == RegistroDoIndice.REGISTRO_ATIVADO &&
					registro.dado.toString().equals(dado.toString()))
				{
					status = deslocamento; // término com êxito, registro já existe
				}
				
				return status;
			}
		);
	}
	
	/**
	 * Procura um registro no bucket com a chave informada.
	 * 
	 * @param chave Chave a ser procurada.
	 * @param dado Dado que corresponde à chave.
	 * 
	 * @return {@code null} se o registro não for encontrado;
	 * o dado correspondente à chave caso contrário.
	 */
	
	protected TIPO_DOS_DADOS pesquisarDadoPelaChave(TIPO_DAS_CHAVES chave)
	{
		TIPO_DOS_DADOS dado = null;
		
		int enderecoDoRegistro = pesquisarEnderecoDaChave(chave);
		
		if (enderecoDoRegistro > 0)
		{
			registroDoIndice.lerBytes(bucket, enderecoDoRegistro);
			dado = registroDoIndice.dado;
		}
		
		return dado;
	}
	
	/**
	 * Procura um registro no bucket com o dado informado.
	 * 
	 * @param dado Dado que corresponde à chave.
	 * 
	 * @return {@code null} se o registro não for encontrado;
	 * a chave correspondente ao dado caso contrário.
	 */
	
	protected TIPO_DAS_CHAVES pesquisarChavePeloDado(TIPO_DOS_DADOS dado)
	{
		TIPO_DAS_CHAVES chave = null;
		
		int enderecoDoRegistro = pesquisarEnderecoDoDado(dado);
		
		if (enderecoDoRegistro > 0)
		{
			registroDoIndice.lerBytes(bucket, enderecoDoRegistro);
			chave = registroDoIndice.chave;
		}
		
		return chave;
	}
	
	/**
	 * Exclui o registro no bucket com a chave e o dado
	 * informados.
	 * 
	 * @param chave Chave a ser procurada.
	 * @param dado Dado que corresponde à chave.
	 * 
	 * @return {@code true} se tudo der certo;
	 * {@code false} caso contrário.
	 */
	
	protected boolean excluir(TIPO_DAS_CHAVES chave, TIPO_DOS_DADOS dado)
	{
		int enderecoDoRegistro = pesquisarEnderecoDaChaveEDoDado(chave, dado);
		
		if (enderecoDoRegistro > 0)
		{
			bucket[enderecoDoRegistro] = RegistroDoIndice.REGISTRO_DESATIVADO;
		}
		
		return enderecoDoRegistro > 0;
	}
	
	/**
	 * Exclui todos os registros com a chave informada.
	 * 
	 * @param chave Chave a ser procurada.
	 */
	
	protected void excluirRegistrosComAChave(TIPO_DAS_CHAVES chave)
	{
		percorrerRegistros(
			(registro, deslocamento) ->
			{
				int status = 0; // indica que o processo deve continuar
				
				if (registro.lapide == RegistroDoIndice.REGISTRO_ATIVADO &&
					registro.chave.toString().equals(chave.toString()))
				{
					bucket[deslocamento] = RegistroDoIndice.REGISTRO_DESATIVADO;
				}
				
				return status;
			}
		);
	}
	
	/**
	 * Tenta excluir o primeiro registro com a chave informada.
	 * 
	 * @param chave Chave a ser procurada.
	 * 
	 * @return {@code true} se tudo der certo;
	 * {@code false} caso contrário.
	 */
	
	protected boolean excluir(TIPO_DAS_CHAVES chave)
	{
		int enderecoDoRegistro = pesquisarEnderecoDaChave(chave);
		
		if (enderecoDoRegistro > 0)
		{
			bucket[enderecoDoRegistro] = RegistroDoIndice.REGISTRO_DESATIVADO;
		}
		
		return enderecoDoRegistro > 0;
	}
	
	/**
	 * Tenta inserir a chave e o dado no bucket.
	 * 
	 * @param chave Chave a ser inserida.
	 * @param dado Dado que corresponde à chave.
	 * 
	 * @return a profundidade local do bucket se
	 * ele estiver cheio;
	 * {@code -1} se tudo correr bem;
	 * {@code -2} se o par (chave, dado) já existe.
	 */
	
	protected byte inserir(TIPO_DAS_CHAVES chave, TIPO_DOS_DADOS dado)
	{
		byte resultado = -2;
		
		// checa se o par (chave, dado) ainda não existe no bucket
		if (pesquisarEnderecoDaChaveEDoDado(chave, dado) == 0)
		{
			resultado = (byte) percorrerRegistros(
				(registro, deslocamento) ->
				{
					int status = 0; // indica que o processo deve continuar
					
					if (registro.lapide == RegistroDoIndice.REGISTRO_DESATIVADO)
					{
						registro.lapide = RegistroDoIndice.REGISTRO_ATIVADO;
						registro.chave = chave;
						registro.dado = dado;
						
						registro.escreverObjeto(bucket, deslocamento);
						status = -1; // término com êxito, registro inserido
					}
					
					return status;
				}
			);
		}
		
		return ( resultado == 0 ? profundidadeLocal : resultado );
	}
	
	/**
	 * Procura todos os registros com uma chave específica e gera
	 * uma lista com os dados correspondentes a essas chaves.
	 * 
	 * @param chave Chave a ser procurada.
	 * 
	 * @return lista com os dados correspondentes às chaves.
	 */
	
	public ArrayList<TIPO_DOS_DADOS> listarDadosComAChave(TIPO_DAS_CHAVES chave)
	{
		ArrayList<TIPO_DOS_DADOS> lista = new ArrayList<>();
		
		percorrerRegistros(
			(registro, deslocamento) ->
			{
				if (registro.lapide == RegistroDoIndice.REGISTRO_ATIVADO &&
					registro.chave.toString().equals(chave.toString()))
				{
					lista.add(registro.dado);
				}
				
				return 0; // continuar processo
			}
		);
		
		return lista;
	}
	
	/**
	 * Cria uma nova instância de {@link Bucket} com a profundidade
	 * local informada.
	 * 
	 * @param profundidadeLocal Profundidade local do novo bucket.
	 * 
	 * @return uma nova instância de {@link Bucket} com a profundidade
	 * local informada.
	 */
	
	public Bucket<TIPO_DAS_CHAVES, TIPO_DOS_DADOS> criarBucket(byte profundidadeLocal)
	{
		return new Bucket<TIPO_DAS_CHAVES, TIPO_DOS_DADOS>(
			profundidadeLocal,
			numeroDeRegistrosPorBucket,
			registroDoIndice.quantidadeMaximaDeBytesParaAChave,
			registroDoIndice.quantidadeMaximaDeBytesParaODado,
			registroDoIndice.construtorDaChave,
			registroDoIndice.construtorDoDado
		);
	}
	
	/**
	 * Cria uma nova instância de {@link Bucket} com a profundidade
	 * local deste bucket.
	 * 
	 * @return uma nova instância de {@link Bucket} com a profundidade
	 * local deste bucket.
	 */
	
	public Bucket<TIPO_DAS_CHAVES, TIPO_DOS_DADOS> criarBucket()
	{
		return criarBucket(profundidadeLocal);
	}
	
	@Override
	public Bucket<TIPO_DAS_CHAVES, TIPO_DOS_DADOS> clone()
	{
		return new Bucket<TIPO_DAS_CHAVES, TIPO_DOS_DADOS>(
				Arrays.copyOf(bucket, bucket.length),
				profundidadeLocal,
				numeroDeRegistrosPorBucket,
				registroDoIndice.quantidadeMaximaDeBytesParaAChave,
				registroDoIndice.quantidadeMaximaDeBytesParaODado,
				registroDoIndice.construtorDaChave,
				registroDoIndice.construtorDoDado
			);
	}
	
	@Override
	public byte[] obterBytes()
	{
		return bucket;
	}

	@Override
	public void lerBytes(byte[] bytes)
	{
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		
		try
		{
			byteArrayInputStream.read(bucket);
			this.profundidadeLocal = bucket[0];
			
			byteArrayInputStream.close();
		}
		
		catch (IOException ioex)
		{
			ioex.printStackTrace();
		}
	}
	
	public String toString(String delimitadorEntreRegistros,
		String delimitadorEntreOsCamposDoRegistro, boolean mostrarApenasAsChaves)
	{
		toStr = "[ ";
		
		percorrerRegistros(
			(registro, deslocamento) ->
			{
				toStr +=
				"{" +
				registro.toString(delimitadorEntreOsCamposDoRegistro, mostrarApenasAsChaves) +
				"}" + delimitadorEntreRegistros;
				
				return 0; // continuar processo
			}
		);
		
		toStr = toStr.substring(0, toStr.length() - delimitadorEntreRegistros.length()) + " ]";
		
		return toStr;
	}
	
	@Override
	public String toString()
	{
		return toString(", ", ", ", false);
	}
}
