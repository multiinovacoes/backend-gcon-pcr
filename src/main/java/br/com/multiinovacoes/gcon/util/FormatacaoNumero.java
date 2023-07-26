package br.com.multiinovacoes.gcon.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Formata numeros
 * 
 * @author evanildo
 *
 */
public class FormatacaoNumero {
	
	/**
	 * construtor default of FormatacaoNumero
	 */
	public FormatacaoNumero() {
		super();
	}
	
	/**
	 * Formata o valor recebido em moeda, definida como default no Sistema Operacional
	 * 
	 * @param valor o valor a ser formatado
	 * 
	 * @return o valor formatado
	 */
	public static String formatarMoeda( String valor ){
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMinimumFractionDigits( 2 );
		formatter.setMaximumFractionDigits( 2 );
		return formatter.format( Double.parseDouble( valor) );
	}
	
	/**
	 * Formata o valor recebido em moeda, definida como default no Sistema Operacional
	 * 
	 * @param valor o valor a ser formatado
	 * 
	 * @return o valor formatado
	 */
	public static String formatarMoeda( double valor ){
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMinimumFractionDigits( 2 );
		formatter.setMaximumFractionDigits( 2 );
		return formatter.format( valor );
	}
	
	/**
	 * Metodo de formata��o de dados
	 * 
	 * @param valor o valor a formatar
	 * autor : hugo
	 * 
	 * @return o valor formatado
	 */
	public static String getValor(double valor){
		DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,##0.00");
		df.setMinimumFractionDigits( 2 ); // incluido em 20050812 - evanildo m batista
		String valorFormatado = df.format(valor);
		return valorFormatado; 
	}
	
	public static double getValor(String valorStr) throws ParseException{
		DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,##0.00");
		df.setMinimumFractionDigits( 2 ); // incluido em 20050812 - evanildo m batista
		double valor = df.parse(valorStr).doubleValue();
		return valor; 
	}
	
	/**
	 * Arredonda um valor decimal com o numero de casas decimais informadas no parametro.
	 * 
	 * @param valorDecimal o valor a ser arredondado
	 * @param numeroCasasDecimais o numero de casas decimais desejadas
	 * 
	 * @return o novo valor arredondado para N numeroCasasDecimais
	 * 
	 * @throws NumberFormatException
	 */	
	public static double arredondarCasasDecimais( double valorDecimal, int numeroCasasDecimais  )throws ParseException {
		return arredondarCasasDecimais( new Double( valorDecimal ), numeroCasasDecimais  ).doubleValue();
	}
	
	
	/**
	 * Arredonda um valor decimal com o numero de casas decimais informadas no parametro.
	 * 
	 * Recebe um valor Double, converte-o para uma String formatada
	 * com o devido formato de casas decimais desejadas (conforme
	 * o metodo format da classe Format) e cria um novo valorDecimal,
	 * instancia da classe Double, com as casas decimais arredondadas
	 * 
	 * @param valorDecimal o valor a ser arredondado
	 * @param numeroCasasDecimais o numero de casas decimais desejadas
	 * 
	 * @return o novo valor arredondado para N numeroCasasDecimais
	 * 
	 * @throws NumberFormatException
	 */
	public static Double arredondarCasasDecimais( Double valorDecimal, int numeroCasasDecimais )throws ParseException {
		
		String formatoDecimal = "0.";
		
		for( int i = 0; i < numeroCasasDecimais; i++ ){
			formatoDecimal += "0";
		}
		
		// obtem o formato a ser usado
		Format fmt = new DecimalFormat( formatoDecimal );
		
		try {
			
			// recupera o valor formatado, em uma string
			String strValor = fmt.format( valorDecimal );
			
			// converte a string strValor para um tipo double usando o locale alemao, para ficar no formato 
			// utilizado pelo tipo double, ou seja, usando o ponto (.) como separador decimal
			// double tempValor = NumberFormat.getNumberInstance(Locale.GERMAN).parse( strValor ).doubleValue();
			double tempValor = NumberFormat.getNumberInstance().parse( strValor ).doubleValue();
			
			return new Double( tempValor );
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		try {
			
			return new Double( fmt.format( valorDecimal ) );
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	/**
	 * @param valorDecimal valor a truncar
	 * @param numeroCasasDecimais o numero de casas decimais do valor
	 * 
	 * @return o valor truncado para o numero de casas decimais informado
	 */
	public static BigDecimal truncarDecimal( BigDecimal valorDecimal, int numeroCasasDecimais ) {
		return valorDecimal.setScale( numeroCasasDecimais, BigDecimal.ROUND_DOWN );
	}
	
	/**
	 * @param valorDecimal valor a truncar
	 * @param numeroCasasDecimais o numero de casas decimais do valor
	 * 
	 * @return o valor truncado para o numero de casas decimais informado
	 */
	public static double truncarDecimal( double valorDecimal, int numeroCasasDecimais ) {
		return truncarDecimal( new BigDecimal( String.valueOf( valorDecimal ) ), numeroCasasDecimais ).doubleValue();
	}
	
	/**
	 * @param valorDecimal valor a truncar
	 * @param numeroCasasDecimais o numero de casas decimais do valor
	 * 
	 * @return o valor truncado para o numero de casas decimais informado
	 */
	public static Double truncarDecimal( Double valorDecimal, int numeroCasasDecimais ) {
		return new Double( truncarDecimal( (valorDecimal!=null?valorDecimal.doubleValue():0), numeroCasasDecimais ) ); 
	}
	
	/**
	 * @param valorDecimal o valor a arredondar
	 * @param numeroCasasDecimais o numero de casas decimais do valor
	 * 
	 * @return o valor arredondado para o numero de casas decimais informado
	 */
	public static BigDecimal arredondarDecimal( BigDecimal valorDecimal, int numeroCasasDecimais ) {
		return valorDecimal.setScale( numeroCasasDecimais, BigDecimal.ROUND_HALF_UP );
	}
	
	/**
	 * @param valorDecimal o valor a arredondar
	 * @param numeroCasasDecimais o numero de casas decimais do valor
	 * 
	 * @return o valor arredondado para o numero de casas decimais informado
	 */
	public static double arredondarDecimal( double valorDecimal, int numeroCasasDecimais ) {
		return arredondarDecimal( new BigDecimal( String.valueOf( valorDecimal ) ), numeroCasasDecimais ).doubleValue();
	}
	
	/**
	 * @param valorDecimal o valor a arredondar
	 * @param numeroCasasDecimais o numero de casas decimais do valor
	 * 
	 * @return o valor arredondado para o numero de casas decimais informado
	 */
	public static Double arredondarDecimal( Double valorDecimal, int numeroCasasDecimais ) {
		return new Double( arredondarDecimal( (valorDecimal != null?valorDecimal.doubleValue():0), numeroCasasDecimais ) );
	}
	
	/**
	 * Retorna um valor, passado como parametro, no formato de porcentagem e arredondamento de 2 casas decimais
	 * 
	 * Esse metodo e utilizado no lugar do correspondente  
	 * java.text.NumberFormat.getPercentInstance() porque este ultimo retorna o formato
	 * de porcentagem como uma String e concatenado com o simbolo de porcentagem (%)
	 * e o presente metodo retorna um valor numerico relativo ao formato de porcentagem,
	 * mas sem o simbolo '%'
	 * 
	 * @param valor o valor a ser formatado para porcentagem
	 * 
	 * @return o valor formatado
	 * 
	 * @throws ParseException
	 */
	public static double formatarPorcentagem( double valor )throws ParseException {
		
		if( valor == 0 ){
			return 0;
		}else{
			// obtem o valor em formato percentual ( multiplicado por 100 )
			return arredondarCasasDecimais( ( valor*100 ), 2 );
		}
		
	}
	
	/**
	 * Retorna um valor, passado como parametro, no formato de porcentagem e arredondamento de 2 casas decimais
	 * 
	 * Esse metodo e utilizado no lugar do correspondente  
	 * java.text.NumberFormat.getPercentInstance() porque este ultimo retorna o formato
	 * de porcentagem como uma String e concatenado com o simbolo de porcentagem (%)
	 * e o presente metodo retorna um valor numerico relativo ao formato de porcentagem,
	 * mas sem o simbolo '%'
	 * 
	 * @param valor o valor a ser formatado para porcentagem
	 * 
	 * @return o valor formatado
	 * 
	 * @throws ParseException
	 */
	public static Double formatarPorcentagem( Double valor )throws ParseException {
		return new Double( formatarPorcentagem( valor != null ? valor.doubleValue() : 0  ) );
	}
	
	/**
	 * Formata o CEP no padrao brasileiro
	 * 
	 * @param cep o cep a ser formatado
	 * 
	 * @return o cep formatado
	 */
	public static String formatarCep( Integer cep ){
		// formato de CEP brasileiro 00000-000
		NumberFormat fmt = new DecimalFormat("00000000");
		String temp = fmt.format(  cep );
		String cepFormatado = temp.substring(0,5).concat("-").concat( temp.substring( 5 ) ) ;
		
		return cepFormatado;
	}
	
	/**
	 * Formata o numero de telefone
	 * 
	 * @param ddd
	 * @param telefone
	 * @return
	 */
	public static String formatarTelefone( Integer ddd, Integer telefone ){
		String dddStr = ddd != null ? ddd.toString().trim() : null;
		String telefoneStr = telefone != null ? telefone.toString() : null;
		return formatarTelefone( dddStr, telefoneStr );
	}
	
	
	public static String formatarTelefone( String ddd, String telefone ){
		
		StringBuffer telefoneFormatado = new StringBuffer( "" );
		if( telefone != null ) {
			
			if( ddd != null && !ddd.trim().equals( "" ) ) {
				telefoneFormatado.append( "(" );
				telefoneFormatado.append( ddd );
				telefoneFormatado.append( ")" );
			}
			
			int tam = telefone.length();
			if( tam > 4 ){
				int indice = tam - 4;
				telefoneFormatado.append( telefone.substring( 0, indice ) );
				telefoneFormatado.append( "-" );
				telefoneFormatado.append( telefone.substring( indice ) );
			}else {
				telefoneFormatado.append( telefone );
			}
		}
		return telefoneFormatado.toString();
	}
	
	
	/**
	 * Formata o numero preenchendo n zeros a esquerda, ate completar
	 * a quantidade de digitosFormato
	 * 
	 * @param numero o numero a ser formatado
	 * @param digitosFormato a quantidade de digitos do padrao formato
	 * 
	 * @return o numero formatado conforme o padrao
	 */
	public static String formatarZerosEsquerda( Number numero, int digitosFormato ){
		StringBuffer pattern = new StringBuffer();
		for (int i = 0; i < digitosFormato; i++) {
			pattern.append( "0" );// padrao de preenchimento ZERO
		}
		Format fmt = new DecimalFormat( pattern.toString() );
		return fmt.format( numero );
	}
	
	
	/**
	 * Formata o numero preenchendo n zeros a esquerda, ate completar
	 * a quantidade de digitosFormato
	 * 
	 * @param numero o numero a ser formatado
	 * @param digitosFormato a quantidade de digitos do padrao formato
	 * 
	 * @return o numero formatado conforme o padrao
	 */
	public static String formatarZerosEsquerda( int numero, int digitosFormato ){
		return  formatarZerosEsquerda( new Integer( numero ), digitosFormato );
	}
	
	public static String formatarMoeda(BigDecimal valor){
		NumberFormat nf = NumberFormat.getInstance( new Locale( "pt_br" ) );  
		return nf.format( valor );
	}
	
	public static String tirarCasasDecimais(double valor) {
		String valorFormatado = Double.toString(valor);
		if(valorFormatado.contains(".")) {
			valorFormatado= valorFormatado.substring(0, valorFormatado.indexOf("."));
		}
		return valorFormatado;	
	}
	
}
