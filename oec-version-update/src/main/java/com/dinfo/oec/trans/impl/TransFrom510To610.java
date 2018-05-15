package com.dinfo.oec.trans.impl;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.dinfo.model.model.SclassCorpus;
import com.dinfo.model.onto.bean.BaseResource;
import com.dinfo.model.onto.bean.CclassifyResource;
import com.dinfo.model.onto.bean.ExtractInfo;
import com.dinfo.model.onto.bean.ExtractResource;
import com.dinfo.model.onto.bean.MultitupleResource;
import com.dinfo.model.onto.bean.SclassifyResource;
import com.dinfo.model.onto.dto.CclassifyExpression;
import com.dinfo.model.onto.dto.CclassifyNode;
import com.dinfo.model.onto.dto.ConceptNode;
import com.dinfo.model.onto.dto.ConceptNodeValue;
import com.dinfo.model.onto.dto.ElementNode;
import com.dinfo.model.onto.dto.ElementNodeValue;
import com.dinfo.model.onto.dto.ExtractNode;
import com.dinfo.model.onto.dto.SclassifyNode;
import com.dinfo.model.onto.dto.TupleInfo;
import com.dinfo.model.onto.model.MultiTupEntity;
import com.dinfo.model.onto.model.TupMemEntity;
import com.dinfo.model.onto.model.TupMemRelEntity;
import com.dinfo.oec.core.model.bean.OecCclassifyModelBean;
import com.dinfo.oec.core.model.bean.OecCclassifyResourceModelBean;
import com.dinfo.oec.core.model.bean.OecConceptModelBean;
import com.dinfo.oec.core.model.bean.OecConceptResourceModelBean;
import com.dinfo.oec.core.model.bean.OecElementModelBean;
import com.dinfo.oec.core.model.bean.OecElementResourceModelBean;
import com.dinfo.oec.core.model.bean.OecExtractModelBean;
import com.dinfo.oec.core.model.bean.OecMultiTupleModelBean;
import com.dinfo.oec.core.model.bean.OecOntologyModelBean;
import com.dinfo.oec.core.model.bean.OecProjectModelBean;
import com.dinfo.oec.core.model.bean.OecSclassifyCorpusModelBean;
import com.dinfo.oec.core.model.bean.OecSclassifyModelBean;
import com.dinfo.oec.core.model.bean.OecTupleMemberModelBean;
import com.dinfo.oec.core.model.bean.OecTupleMemberRelateModelBean;
import com.dinfo.oec.core.model.bean.OecTupleModelBean;
import com.dinfo.oec.tools.ModelXmlUtilsOEC610;
import com.dinfo.oec.tools.PrimaryKeyGenerator;
import com.dinfo.oec.trans.inter.ModelVersionTransInter;

public class TransFrom510To610 implements ModelVersionTransInter {

	private static final Logger logger = Logger.getLogger(TransFrom510To610.class);

	public Object load(String inFilePath) throws IOException, ClassNotFoundException {
		ZipInputStream zis = new ZipInputStream(new FileInputStream(inFilePath));
		zis.getNextEntry();
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(zis);
			OecProjectModelBean oecProjectModelBean = (OecProjectModelBean) ois.readObject();
			oecProjectModelBean.setProjectCode("PR00001");
			return oecProjectModelBean;
		} finally {
			if (ois != null) {
				ois.close();
			}
		}
	}

	public Object transe(Object obj) {
		Map<Class<?>, Object> resultMap = new HashMap<Class<?>, Object>();
		OecProjectModelBean oecProjectModelBean = (OecProjectModelBean) obj;
		// 获取要素、概念资源
		BaseResource baseResource = new BaseResource();
		initBaesResource(baseResource, oecProjectModelBean);
		resultMap.put(BaseResource.class, baseResource);
		// C分类
		CclassifyResource cclassifyResource = new CclassifyResource();
		initCclassifyResource(cclassifyResource, oecProjectModelBean);
		resultMap.put(CclassifyResource.class, cclassifyResource);
		// S分类
		SclassifyResource sclassifyResource = new SclassifyResource();
//		initSclassifyResource(sclassifyResource,oecProjectModelBean);
		resultMap.put(SclassifyResource.class,sclassifyResource);
		// 信息抽取
		ExtractResource extractResource = new ExtractResource();
//		initExtractResource(extractResource, oecProjectModelBean);
		resultMap.put(ExtractResource.class, extractResource);
		// 多元组
		MultitupleResource multitupleResource = new MultitupleResource();
		initMultitupleResource(multitupleResource, oecProjectModelBean);
		resultMap.put(MultitupleResource.class, multitupleResource);
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	public void save(String inFilePath, String outPath) throws IOException, ClassNotFoundException {
		OecProjectModelBean oecProjectModelBean = (OecProjectModelBean) load(inFilePath);
		Map<String, Long> keyMap = rebuidId(oecProjectModelBean);
		Map<Class<?>, Object> resultMap = (Map<Class<?>, Object>) transe(oecProjectModelBean);
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("utf-8");
		Element schema = doc.addElement("model");
		schema.addAttribute("id",PrimaryKeyGenerator.getKeya().toString());
		schema.addAttribute("name", oecProjectModelBean.getProjectName());
		schema.addAttribute("appId", "0");
		schema.addAttribute("status", "1");
		schema.addAttribute("note", "版本升级生成");
		Element ontology = schema.addElement("ontology");
		// c分类资源获取及转换
		CclassifyResource cclassifyResource = (CclassifyResource) resultMap.get(CclassifyResource.class);
		if (cclassifyResource != null) {
			Map<String, List<CclassifyNode>> treeNodeListMap = cclassifyResource.getTree_nodelist();
			if (treeNodeListMap != null) {
				for (Map.Entry<String, List<CclassifyNode>> entry : treeNodeListMap.entrySet()) {
					ModelXmlUtilsOEC610.createCcOntologyTree(ontology, entry.getValue());
				}
			}
		} else {
			logger.error("parseToXml cclassifyResource为null");
		}
		// s分类资源获
		SclassifyResource sclassifyResource = (SclassifyResource) resultMap.get(SclassifyResource.class);
		if (sclassifyResource != null) {
			Map<Long, Map<Integer, List<SclassifyNode>>> root_Level_Nodes = sclassifyResource
					.getRoot_Level_Nodes();
			if(root_Level_Nodes!=null){
				for (Map.Entry<Long,Map<Integer,List<SclassifyNode>>> entry : root_Level_Nodes.entrySet()) {
					ModelXmlUtilsOEC610.createScOntologyTree(ontology,entry.getValue());
				}
			}
		} else {
			logger.error("parseToXml sclassifyResource为null");
		}
		// 多元组
		MultitupleResource multitupleResource = (MultitupleResource) resultMap.get(MultitupleResource.class);
		if(multitupleResource!=null){
			Map<String, MultiTupEntity> mulTupMap = multitupleResource.getMulTupMap();
			Map<String, List<TupleInfo>> tupleInfoMap = multitupleResource.getTupleInfoMap();
			if(tupleInfoMap==null){
				tupleInfoMap=new HashMap<String, List<TupleInfo>>(0);
			}
			Map<String, List<TupMemRelEntity>> memRelateMap = multitupleResource.getMemRelateMap();
			if(memRelateMap==null){
				memRelateMap=new HashMap<String, List<TupMemRelEntity>>(0);
			}
			if(mulTupMap!=null){
				for (Map.Entry<String,MultiTupEntity>  entry : mulTupMap.entrySet()) {
					MultiTupEntity multiTupEntity=entry.getValue();
					List<TupleInfo> tupleInfos=tupleInfoMap.get(entry.getKey());
					List<TupMemRelEntity> tupMemRelEntities=memRelateMap.get(entry.getKey());
					ModelXmlUtilsOEC610.createRmOntologyTree(ontology,multiTupEntity,tupleInfos,tupMemRelEntities);
				}
			}
		}else{
			logger.error("parseToXml multitupleResource为null");
		}
		// 获取要素、概念资源
		BaseResource baseResource = (BaseResource) resultMap.get(BaseResource.class);
		if (baseResource != null) {
			// 要素
			Map<String, List<ElementNode>> tree_elelist = baseResource.getTree_elelist();
			if (tree_elelist != null && tree_elelist.size() > 0) {
				Element element = schema.addElement("element");
				for (Map.Entry<String, List<ElementNode>> entry : tree_elelist.entrySet()) {
					ModelXmlUtilsOEC610.createElementTree(element, entry.getValue(), entry.getKey());
				}
			}
			// 概念
			Map<String, List<ConceptNode>> tree_conlist = baseResource.getTree_conlist();
			if (tree_conlist != null && tree_conlist.size() > 0) {
				Element concept = schema.addElement("concept");
				for (Map.Entry<String, List<ConceptNode>> entry : tree_conlist.entrySet()) {
					ModelXmlUtilsOEC610.createConceptTree(concept, entry.getValue(), entry.getKey());
				}
			}
		} else {
			logger.error("parseToXml baseResource为null");
		}
		// ==写入文件
		BufferedWriter writer =null;
		try {
			writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPath) , "utf-8"));
			writer.write(doc.asXML());
			writer.flush();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	// ------------------重建编号----------------------------
	private Map<String, Long> rebuidId(OecProjectModelBean oecProjectModelBean) {
		Map<String, Long> keyMap = new HashMap<String, Long>();
		// 要素
		if (oecProjectModelBean.getOecElements() != null) {
			Collections.sort(oecProjectModelBean.getOecElements(),new Comparator<OecElementModelBean>() {
				public int compare(OecElementModelBean o1, OecElementModelBean o2) {
					if(o1.getElementLevel()>o2.getElementLevel()){
						return 1;
					}else if(o1.getElementLevel()<o2.getElementLevel()){
						return -1;
					}
					return 0;
				}
			});
			for (OecElementModelBean oecElementModelBean : oecProjectModelBean.getOecElements()) {
				Long id=PrimaryKeyGenerator.getKeya();
				keyMap.put(oecElementModelBean.getElementId(), id);
				oecElementModelBean.setElementId(id.toString());
				String parentId=oecElementModelBean.getElementParentId();
				oecElementModelBean.setElementParentId(keyMap.containsKey(parentId)?keyMap.get(parentId).toString():"0");
				oecElementModelBean.setElementIdPath(getPath(oecElementModelBean.getElementIdPath(),keyMap));
				oecElementModelBean.setElementLevel(oecElementModelBean.getElementIdPath().split("->").length-1);
				if(oecElementModelBean.getResources()!=null){
					for(OecElementResourceModelBean oecElementResourceModelBean:oecElementModelBean.getResources()){
						oecElementResourceModelBean.setElementId(id.toString());
					}
				}
			}
		}
		// 概念
		if (oecProjectModelBean.getOecConcepts() != null) {
			Collections.sort(oecProjectModelBean.getOecConcepts(),new Comparator<OecConceptModelBean>() {
				public int compare(OecConceptModelBean o1, OecConceptModelBean o2) {
					if(o1.getConceptLevel()>o2.getConceptLevel()){
						return 1;
					}else if(o1.getConceptLevel()<o2.getConceptLevel()){
						return -1;
					}
					return 0;
				}
			});
			for (OecConceptModelBean oecConceptModelBean : oecProjectModelBean.getOecConcepts()) {
				Long id=PrimaryKeyGenerator.getKeya();
				keyMap.put(oecConceptModelBean.getConceptId(), id);
				oecConceptModelBean.setConceptId(id.toString());
				String parentId=oecConceptModelBean.getConceptParentId();
				oecConceptModelBean.setConceptParentId(keyMap.containsKey(parentId)?keyMap.get(parentId).toString():"0");
				oecConceptModelBean.setConceptIdPath(getPath(oecConceptModelBean.getConceptIdPath(), keyMap));
				oecConceptModelBean.setConceptLevel(oecConceptModelBean.getConceptIdPath().split("->").length-1);
				if(oecConceptModelBean.getResources()!=null){
					for(OecConceptResourceModelBean oecConceptResourceModelBean: oecConceptModelBean.getResources()){
						oecConceptResourceModelBean.setConceptId(id.toString());
					}
				}
			}
		}
		// 本体
		if (oecProjectModelBean.getOecOntologys() != null) {
			for (OecOntologyModelBean oecOntologyModelBean : oecProjectModelBean.getOecOntologys()) {
				switch (oecOntologyModelBean.getOntoTypeId()) {
				case 1: {// c分类
					if (oecOntologyModelBean.getOecCclassifys() != null) {
						Collections.sort(oecOntologyModelBean.getOecCclassifys(),new Comparator<OecCclassifyModelBean>() {
							public int compare(OecCclassifyModelBean o1, OecCclassifyModelBean o2) {
								if(o1.getClasLevel()>o2.getClasLevel()){
									return 1;
								}else if(o1.getClasLevel()<o2.getClasLevel()){
									return -1;
								}
								return 0;
							}
						});
						for (OecCclassifyModelBean oecCclassifyModelBean : oecOntologyModelBean.getOecCclassifys()) {
							Long id=PrimaryKeyGenerator.getKeya();
							keyMap.put(oecCclassifyModelBean.getClasId(), id);
							oecCclassifyModelBean.setClasId(id.toString());
							String parentId=oecCclassifyModelBean.getClasParentId();
							oecCclassifyModelBean.setClasParentId(keyMap.containsKey(parentId)?keyMap.get(parentId).toString():"0");
							oecCclassifyModelBean.setClasIdPath(getPath(oecCclassifyModelBean.getClasIdPath(), keyMap));
							oecCclassifyModelBean.setClasLevel(oecCclassifyModelBean.getClasIdPath().split("->").length-1);
							oecCclassifyModelBean.setSyhResourceExpression(transSynResExp(oecCclassifyModelBean.getSyhResourceExpression(), keyMap));
							if(oecCclassifyModelBean.getResources()!=null){
								for(OecCclassifyResourceModelBean oecCclassifyResourceModelBean:oecCclassifyModelBean.getResources()){
									oecCclassifyResourceModelBean.setClasId(id.toString());
								}
							}
						}
						for (OecCclassifyModelBean oecCclassifyModelBean : oecOntologyModelBean.getOecCclassifys()) {
							if(oecCclassifyModelBean.getResources()!=null){
								for(int i=oecCclassifyModelBean.getResources().size()-1;i>=0;i--){
									OecCclassifyResourceModelBean resource=oecCclassifyModelBean.getResources().get(i);
									if(!checkCclassifyExpressionValid(resource,keyMap)){
										oecCclassifyModelBean.getResources().remove(i);
									}
								}
							}
						}
					}
				}
					break;
				case 2: {// s分类
					if (oecOntologyModelBean.getOecSclassifys() != null) {
						Collections.sort(oecOntologyModelBean.getOecSclassifys(),new Comparator<OecSclassifyModelBean>() {
							public int compare(OecSclassifyModelBean o1, OecSclassifyModelBean o2) {
								if(o1.getClasLevel()>o2.getClasLevel()){
									return 1;
								}else if(o1.getClasLevel()<o2.getClasLevel()){
									return -1;
								}
								return 0;
							}
						});
						for (OecSclassifyModelBean oecSclassifyModelBean : oecOntologyModelBean.getOecSclassifys()) {
							Long id=PrimaryKeyGenerator.getKeya();
							keyMap.put(oecSclassifyModelBean.getClasId(), id);
							oecSclassifyModelBean.setClasId(id.toString());
							String parentId=oecSclassifyModelBean.getClasParentId();
							oecSclassifyModelBean.setClasParentId(keyMap.containsKey(parentId)?keyMap.get(parentId).toString():"0");
							oecSclassifyModelBean.setClasIdPath(getPath(oecSclassifyModelBean.getClasIdPath(), keyMap));
							oecSclassifyModelBean.setClasLevel(oecSclassifyModelBean.getClasIdPath().split("->").length-1);
							if(oecSclassifyModelBean.getCorpuss()!=null){
								for(OecSclassifyCorpusModelBean oecSclassifyCorpusModelBean:oecSclassifyModelBean.getCorpuss()){
									oecSclassifyCorpusModelBean.setClasId(id.toString());
								}
							}
						}
					}
				}
					break;
				case 3: {// 信息抽取
					if (oecOntologyModelBean.getOecExtracts() != null) {
						Collections.sort(oecOntologyModelBean.getOecExtracts(),new Comparator<OecExtractModelBean>() {
							public int compare(OecExtractModelBean o1, OecExtractModelBean o2) {
								if(o1.getExtLevel()>o2.getExtLevel()){
									return 1;
								}else if(o1.getExtLevel()<o2.getExtLevel()){
									return -1;
								}
								return 0;
							}
						});
						for (OecExtractModelBean oecExtractModelBean : oecOntologyModelBean.getOecExtracts()) {
							Long id=PrimaryKeyGenerator.getKeya();
							keyMap.put(oecExtractModelBean.getExtId(),id);
							oecExtractModelBean.setExtId(id.toString());
							String parentId=oecExtractModelBean.getExtParentId();
							oecExtractModelBean.setExtParentId(keyMap.containsKey(parentId)?keyMap.get(parentId).toString():"0");
							oecExtractModelBean.setExtIdPath(getPath(oecExtractModelBean.getExtIdPath(), keyMap));
							oecExtractModelBean.setExtLevel(oecExtractModelBean.getExtIdPath().split("->").length-1);
						}
					}
				}
					break;
				case 4: {// 多元组
					if (oecOntologyModelBean.getOecMultiTuples() != null) {
						for (OecMultiTupleModelBean oecMultiTupleModelBean : oecOntologyModelBean.getOecMultiTuples()) {
							Long multiTupleId=PrimaryKeyGenerator.getKeya();
							keyMap.put(oecMultiTupleModelBean.getMultiTupId(), multiTupleId);
							oecMultiTupleModelBean.setMultiTupId(multiTupleId.toString());
							oecMultiTupleModelBean.setSyhResourceExpression(transSynResExp(oecMultiTupleModelBean.getSyhResourceExpression(), keyMap));
							// 元组
							if (oecMultiTupleModelBean.getOecTuples() != null) {
								for (OecTupleModelBean oecTupleModelBean : oecMultiTupleModelBean.getOecTuples()) {
									Long tupleId=PrimaryKeyGenerator.getKeya();
									keyMap.put(oecTupleModelBean.getTupId(),tupleId);
									oecTupleModelBean.setTupId(tupleId.toString());
									oecTupleModelBean.setMultiTupId(multiTupleId.toString());
									// 元组成员
									if (oecTupleModelBean.getTupMembers() != null) {
										for (OecTupleMemberModelBean oecTupleMemberModelBean : oecTupleModelBean
												.getTupMembers()) {
											Long memId=PrimaryKeyGenerator.getKeya();
											keyMap.put(oecTupleMemberModelBean.getMemId(),memId);
											oecTupleMemberModelBean.setMemId(memId.toString());
											oecTupleMemberModelBean.setTupId(tupleId.toString());
											String ecnId=oecTupleMemberModelBean.getEcnId();
											oecTupleMemberModelBean.setEcnId(keyMap.containsKey(ecnId)?keyMap.get(ecnId).toString():PrimaryKeyGenerator.getKeya().toString());
										}
									}
								}
							}
							//元组成员关系
							if(oecMultiTupleModelBean.getMemRelates()!=null){
								for(int i=oecMultiTupleModelBean.getMemRelates().size()-1;i>=0;i--){
									OecTupleMemberRelateModelBean relate=oecMultiTupleModelBean.getMemRelates().get(i);
									if(!keyMap.containsKey(relate.getOriginTupMemId()) || !keyMap.containsKey(relate.getTargetTupMemId())){
										oecMultiTupleModelBean.getMemRelates().remove(i);
									}else{
										relate.setOriginTupMemId(keyMap.get(relate.getOriginTupMemId()).toString());
										relate.setTargetTupMemId(keyMap.get(relate.getTargetTupMemId()).toString());
									}
								}
							}
						}
					}
				}
					break;
				}
			}
		}
		return keyMap;
	}
	
	//转换path
	private static final String getPath(String oldPath,Map<String,Long> keyMap){
		String []ids=oldPath.split("->");
		StringBuilder sb=new StringBuilder();
		for (String id : ids) {
			if(keyMap.containsKey(id)){
				sb.append(keyMap.get(id)+"->");
			}else{
				sb.append(id+="->");
			}
		}
		return sb.toString().substring(0,sb.length()-2);
	}
	
	/**
	 * 检查并转换c分类表达式
	 * @param oecCclassifyResourceModelBean
	 * @param keyMap
	 * @return
	 */
	private boolean checkCclassifyExpressionValid(OecCclassifyResourceModelBean oecCclassifyResourceModelBean,Map<String,Long> keyMap) {
		String resource = oecCclassifyResourceModelBean.getEcidExpression();
		if (resource.contains("@")) {
			// id EXP
			List<Integer> position = new ArrayList<Integer>();
			for (int i = 0; i < resource.length(); i++) {
				char c = resource.charAt(i);
				if ('@' == c) {
					position.add(i);
				}
			}
			if (position.size() % 2 == 1) {
				return false;
			}
			for (int i = 0; i < position.size(); i++) {
				if (i % 2 == 0) {
					resource += resource.substring(0, position.get(i)) + "[" + resource.substring(position.get(i) + 1);
				} else {
					resource += resource.substring(0, position.get(i)) + "]" + resource.substring(position.get(i) + 1);
				}
			}
			oecCclassifyResourceModelBean.setEcidExpression(resource);
			// name EXP
			resource = oecCclassifyResourceModelBean.getEcnameExpression();
			position = new ArrayList<Integer>();
			for (int i = 0; i < resource.length(); i++) {
				char c = resource.charAt(i);
				if ('@' == c) {
					position.add(i);
				}
			}
			if (position.size() % 2 == 1) {
				return false;
			}
			for (int i = 0; i < position.size(); i++) {
				if (i % 2 == 0) {
					if (position.get(i) < resource.length()) {
						resource += resource.substring(0, position.get(i)) + "["
								+ resource.substring(position.get(i) + 1);
					} else {
						resource += resource.substring(0, position.get(i)) + "[";
					}
				} else {
					if (position.get(i) < resource.length()) {
						resource += resource.substring(0, position.get(i)) + "]"
								+ resource.substring(position.get(i) + 1);
					} else {
						resource += resource.substring(0, position.get(i)) + "]";
					}
				}
			}
			oecCclassifyResourceModelBean.setEcnameExpression(resource);

		}
		if (oecCclassifyResourceModelBean.getEcidExpression().contains("&")) {
			oecCclassifyResourceModelBean
					.setEcidExpression(oecCclassifyResourceModelBean.getEcidExpression().replaceAll("&", ""));
			oecCclassifyResourceModelBean
					.setEcnameExpression(oecCclassifyResourceModelBean.getEcnameExpression().replaceAll("&", ""));
		}
		String ecIdExp=oecCclassifyResourceModelBean.getEcidExpression();
		Pattern idPattern = Pattern.compile("[eco]_[a-zA-Z0-9]{8,8}");
		Matcher matcher = idPattern.matcher(ecIdExp);
		while(matcher.find()){
			String group = matcher.group();
			if(!keyMap.containsKey(group.substring(2))){
				return false;
			}
			String newId=keyMap.get(group.substring(2)).toString();
			if(group.startsWith("e_")){//要素
				ecIdExp=ecIdExp.replaceAll(group,"e_"+newId);
			}else if(group.startsWith("c_")){//概念
				ecIdExp=ecIdExp.replaceAll(group,"c_"+newId);
			}else if(group.startsWith("o_")){//本体
				ecIdExp=ecIdExp.replaceAll(group,"o_"+newId);
			}
		}
		oecCclassifyResourceModelBean.setEcidExpression(ecIdExp);
		return true;
	}
	
	/**
	 * 转换句式分析表达式
	 * SSTP:DNO;FLG:1;NODE:5Ps19mFj$SSTP:SNO;FLG:1;NODE:qdUZsXoq$SSTP:SCOM;FLG:1;NODE:5Trmwjta$SSTP:WCOM;FLG:1;NODE:7FTWnk6k
	 * @param synExp
	 * @param keyMap
	 * @return
	 */
	public static final String transSynResExp(String synExp,Map<String,Long> keyMap){
		if(synExp==null||synExp.length()==0){
			return "";
		}
		String newSynExp=synExp;
		String []items=synExp.split("\\$");
		if(items.length>0){
			for (String item : items) {
				String []subItems=item.split(";");
				for (String subItem : subItems) {
					if(subItem.startsWith("NODE:")){
						if(keyMap.containsKey(subItem.substring(5))){
							newSynExp=newSynExp.replaceAll(subItem,"NODE:"+keyMap.get(subItem.substring(5)));
						}else{
							return "";
						}
					}
				}
			}
		}
		return newSynExp;
	}

	// ------------------要素、概念---------------------------

	/**
	 * 填充要素、概念值
	 */
	private void initBaesResource(BaseResource baseResource, OecProjectModelBean oecProjectModelBean) {
		// 要素
		List<OecElementModelBean> oecElementModelBeans = oecProjectModelBean.getOecElements();
		Map<String, List<OecElementModelBean>> rootElementBeanMap = new HashMap<String, List<OecElementModelBean>>();
		for (OecElementModelBean oecElementModelBean : oecElementModelBeans) {
			String[] idPath = oecElementModelBean.getElementIdPath().split("->");
			if (!rootElementBeanMap.containsKey(idPath[0])) {
				rootElementBeanMap.put(idPath[0], new ArrayList<OecElementModelBean>());
			}
			List<OecElementModelBean> beans = rootElementBeanMap.get(idPath[0]);
			if (oecElementModelBean.getElementLevel() == 0) {
				beans.add(0, oecElementModelBean);
			} else {
				beans.add(oecElementModelBean);
			}
		}
		// 转换封装
		Map<String, List<ElementNode>> tree_elelist = new HashMap<String, List<ElementNode>>();
		baseResource.setTree_elelist(tree_elelist);
		for (Entry<String, List<OecElementModelBean>> entry : rootElementBeanMap.entrySet()) {
			List<ElementNode> elementNodes = new ArrayList<ElementNode>();
			List<OecElementModelBean> elementModelBeans = entry.getValue();
			for (OecElementModelBean oecElementModelBean : elementModelBeans) {
				elementNodes.add(transOecElementModelBeanToElementNode(oecElementModelBean, entry.getKey()));
			}
			tree_elelist.put(entry.getKey(), elementNodes);
		}
		// 概念
		List<OecConceptModelBean> oecConceptModelBeans = oecProjectModelBean.getOecConcepts();
		Map<String, List<OecConceptModelBean>> rootConceptBeanMap = new HashMap<String, List<OecConceptModelBean>>();
		for (OecConceptModelBean oecConceptModelBean : oecConceptModelBeans) {
			String[] idPath = oecConceptModelBean.getConceptIdPath().split("->");
			if (!rootConceptBeanMap.containsKey(idPath[0])) {
				rootConceptBeanMap.put(idPath[0], new ArrayList<OecConceptModelBean>());
			}
			List<OecConceptModelBean> beans = rootConceptBeanMap.get(idPath[0]);
			if (oecConceptModelBean.getConceptLevel() == 0) {
				beans.add(0, oecConceptModelBean);
			} else {
				beans.add(oecConceptModelBean);
			}
		}
		// 转换封装
		Map<String, List<ConceptNode>> tree_conlist = new HashMap<String, List<ConceptNode>>();
		baseResource.setTree_conlist(tree_conlist);
		for (Entry<String, List<OecConceptModelBean>> entry : rootConceptBeanMap.entrySet()) {
			List<ConceptNode> conceptNodes = new ArrayList<ConceptNode>();
			List<OecConceptModelBean> conceptModelBeans = entry.getValue();
			for (OecConceptModelBean oecConceptModelBean : conceptModelBeans) {
				conceptNodes.add(transOecConceptModelBeanToConceptNode(oecConceptModelBean, entry.getKey()));
			}
			tree_conlist.put(entry.getKey(), conceptNodes);
		}

	}

	/**
	 * 将OecElementModelBean 转化为 ElementNode
	 * 
	 * @param oecElementModelBean
	 * @return
	 */
	private ElementNode transOecElementModelBeanToElementNode(OecElementModelBean oecElementModelBean,
			String topTreeId) {
		ElementNode elementNode = new ElementNode();
		elementNode.setId(oecElementModelBean.getElementId());
		elementNode.setId_path(oecElementModelBean.getElementIdPath());
		elementNode.setLangueId(getLangueId("" + oecElementModelBean.getLangueId()));
		elementNode.setLevel(oecElementModelBean.getElementLevel());
		elementNode.setName(oecElementModelBean.getElementName());
		elementNode.setName_path("");
		elementNode.setNote(oecElementModelBean.getDescription() == null ? "" : oecElementModelBean.getDescription());
		elementNode.setParentId(oecElementModelBean.getElementParentId());
		elementNode.setSort(oecElementModelBean.getElementSort());
		elementNode.setTopTreeId(topTreeId);
		elementNode.setWeight(0);
		List<ElementNodeValue> valueList = new ArrayList<ElementNodeValue>();
		for (OecElementResourceModelBean oecElementResourceModelBean : oecElementModelBean.getResources()) {
			ElementNodeValue elementNodeValue = new ElementNodeValue();
			elementNodeValue.setId("");
			elementNodeValue.setType(oecElementResourceModelBean.getType());
			elementNodeValue.setValidFlg(oecElementResourceModelBean.getValidFlg());
			elementNodeValue.setValue(oecElementResourceModelBean.getElementResource());
			valueList.add(elementNodeValue);
		}
		elementNode.setValueList(valueList);
		return elementNode;
	}

	/**
	 * 将OecElementModelBean 转化为 ElementNode
	 * 
	 * @param oecElementModelBean
	 * @return
	 */
	private ConceptNode transOecConceptModelBeanToConceptNode(OecConceptModelBean oecConceptModelBean,
			String topTreeId) {
		ConceptNode conceptNode = new ConceptNode();
		conceptNode.setId(oecConceptModelBean.getConceptId());
		conceptNode.setId_path(oecConceptModelBean.getConceptIdPath());
		conceptNode.setLangueId(getLangueId("" + oecConceptModelBean.getLangueId()));
		conceptNode.setLevel(oecConceptModelBean.getConceptLevel());
		conceptNode.setName(oecConceptModelBean.getConceptName());
		conceptNode.setName_path("");
		conceptNode.setNote(oecConceptModelBean.getDescription() == null ? "" : oecConceptModelBean.getDescription());
		conceptNode.setParentId(oecConceptModelBean.getConceptParentId());
		conceptNode.setSort(oecConceptModelBean.getConceptSort());
		conceptNode.setTopTreeId(topTreeId);
		conceptNode.setWeight(0);
		List<ConceptNodeValue> valueList = new ArrayList<ConceptNodeValue>();
		for (OecConceptResourceModelBean oecConceptResourceModelBean : oecConceptModelBean.getResources()) {
			ConceptNodeValue conceptNodeValue = new ConceptNodeValue();
			conceptNodeValue.setId("");
			conceptNodeValue.setType(oecConceptResourceModelBean.getType());
			conceptNodeValue.setValidFlg(oecConceptResourceModelBean.getValidFlg());
			conceptNodeValue.setValue(oecConceptResourceModelBean.getConceptResource());
			valueList.add(conceptNodeValue);
		}
		conceptNode.setValueList(valueList);
		return conceptNode;
	}

	// -------------------c分类------------------------------

	private void initCclassifyResource(CclassifyResource cclassifyResource, OecProjectModelBean oecProjectModelBean) {
		// C分类
		Map<String, List<OecCclassifyModelBean>> rootClassifyModelBeanMap = new HashMap<String, List<OecCclassifyModelBean>>();
		List<OecOntologyModelBean> oecOntologys = oecProjectModelBean.getOecOntologys();
		for (OecOntologyModelBean oecOntologyModelBean : oecOntologys) {
			if (oecOntologyModelBean.getOntoTypeId().equals(1)) {// c分类
				List<OecCclassifyModelBean> oecCclassifys = oecOntologyModelBean.getOecCclassifys();
				for (OecCclassifyModelBean oecCclassifyModelBean : oecCclassifys) {
					String[] idPath = oecCclassifyModelBean.getClasIdPath().split("->");
					if (!rootClassifyModelBeanMap.containsKey(idPath[0])) {
						rootClassifyModelBeanMap.put(idPath[0], new ArrayList<OecCclassifyModelBean>());
					}
					List<OecCclassifyModelBean> beans = rootClassifyModelBeanMap.get(idPath[0]);
					if (oecCclassifyModelBean.getClasLevel() == 0) {
						beans.add(0, oecCclassifyModelBean);
					} else {
						beans.add(oecCclassifyModelBean);
					}
				}
			}
		}
		// 转换c分类
		Map<String, List<CclassifyNode>> nodeListMap = new HashMap<String, List<CclassifyNode>>();
		cclassifyResource.setTree_nodelist(nodeListMap);
		for (Entry<String, List<OecCclassifyModelBean>> entry : rootClassifyModelBeanMap.entrySet()) {
			List<CclassifyNode> cclassifyNodes = new ArrayList<CclassifyNode>();
			List<OecCclassifyModelBean> cclassifyModelBeans = entry.getValue();
			for (OecCclassifyModelBean oecCclassifyModelBean : cclassifyModelBeans) {
				cclassifyNodes.add(transOecCclassifyModelBeanToCclassifyNode(oecCclassifyModelBean, entry.getKey()));
			}
			nodeListMap.put(entry.getKey(), cclassifyNodes);
		}
	}

	/**
	 * 将OecCclassifyModelBean 转化为CclassifyNode
	 * 
	 * @param oecCclassifyModelBean
	 * @param key
	 * @return
	 */
	private CclassifyNode transOecCclassifyModelBeanToCclassifyNode(OecCclassifyModelBean oecCclassifyModelBean,
			String topTreeId) {
		CclassifyNode cclassifyNode = new CclassifyNode();
		cclassifyNode.setId(oecCclassifyModelBean.getClasId());
		cclassifyNode.setId_path(oecCclassifyModelBean.getClasIdPath());
		cclassifyNode.setTopTreeId(topTreeId);
		cclassifyNode.setName(oecCclassifyModelBean.getClasName());
		cclassifyNode.setName_path(null);
		cclassifyNode.setLevel(oecCclassifyModelBean.getClasLevel());
		cclassifyNode
				.setNote(oecCclassifyModelBean.getDescription() == null ? "" : oecCclassifyModelBean.getDescription());
		cclassifyNode.setParentId(oecCclassifyModelBean.getClasParentId());
		cclassifyNode.setSort(oecCclassifyModelBean.getClasSort());
		cclassifyNode.setSplit_flg(0);
		cclassifyNode.setSplit_symbol("");
		cclassifyNode.setSyn_flg(oecCclassifyModelBean.getSyhType());
		cclassifyNode.setSyn_exp(oecCclassifyModelBean.getSyhResourceExpression());
		cclassifyNode.setEditFlg(1);
		cclassifyNode.setWeight(0);
		List<CclassifyExpression> cclassifyExpressions = new ArrayList<CclassifyExpression>();
		for (OecCclassifyResourceModelBean oecCclassifyResourceModelBean : oecCclassifyModelBean.getResources()) {
			CclassifyExpression cclassifyExpression = new CclassifyExpression();
			String ecidExp=oecCclassifyResourceModelBean.getEcidExpression();
			String ecnameExp=oecCclassifyResourceModelBean.getEcnameExpression();
			String synExp="";
			if(ecidExp.contains("$")){
				//编号表达式
				int lastIndex=ecidExp.lastIndexOf("$");
				synExp=ecidExp.substring(0, lastIndex);
				ecidExp=ecidExp.substring(lastIndex+1);
				//名称表达式
				lastIndex=ecnameExp.lastIndexOf("$");
				ecnameExp=ecnameExp.substring(lastIndex+1);
			}
			cclassifyExpression.setId(null);
			cclassifyExpression.setIdentFlg(oecCclassifyResourceModelBean.getIdentifyFlg());
			cclassifyExpression.setFactor(oecCclassifyResourceModelBean.getFactor());
			cclassifyExpression.setLanguageId(getLangueId("" + oecCclassifyResourceModelBean.getLangueId()));
			cclassifyExpression.setIdExp(ecidExp);
			cclassifyExpression.setNameExp(ecnameExp);
			cclassifyExpression.setSplitFlg(0);
			cclassifyExpression.setSplitSymbol("");
			cclassifyExpression.setSynAnalyExp(synExp);
			cclassifyExpression.setValidFlg(oecCclassifyResourceModelBean.getValidFlg());
			cclassifyExpression.setSrcIdExp(null);
			cclassifyExpression.setSrcNameExp(null);
			cclassifyExpression.setMatchConinfo(null);
			cclassifyExpression.setMatchEleinfo(null);
			cclassifyExpression.setMatchKeyword(null);
			cclassifyExpression.setClasId(oecCclassifyModelBean.getClasId());
			cclassifyExpressions.add(cclassifyExpression);
		}
		cclassifyNode.setExp_list(cclassifyExpressions);
		return cclassifyNode;
	}
	
	// -------------------s分类-------------------------------
	private void initSclassifyResource(SclassifyResource sclassifyResource, OecProjectModelBean oecProjectModelBean) {
		//s分类
		Map<String, List<OecSclassifyModelBean>> rootSclassifyModelBeanMap = new HashMap<String, List<OecSclassifyModelBean>>();
		List<OecOntologyModelBean> oecOntologys = oecProjectModelBean.getOecOntologys();
		for (OecOntologyModelBean oecOntologyModelBean : oecOntologys) {
			if (oecOntologyModelBean.getOntoTypeId().equals(2)) {// s分类
				List<OecSclassifyModelBean> oecSclassifyModelBeans = oecOntologyModelBean.getOecSclassifys();
				for (OecSclassifyModelBean oecSclassifyModelBean : oecSclassifyModelBeans) {
					String[] idPath = oecSclassifyModelBean.getClasIdPath().split("->");
					if (!rootSclassifyModelBeanMap.containsKey(idPath[0])) {
						rootSclassifyModelBeanMap.put(idPath[0], new ArrayList<OecSclassifyModelBean>());
					}
					List<OecSclassifyModelBean> beans = rootSclassifyModelBeanMap.get(idPath[0]);
					if (oecSclassifyModelBean.getClasLevel() == 0) {
						beans.add(0, oecSclassifyModelBean);
					} else {
						beans.add(oecSclassifyModelBean);
					}
				}
			}
		}
		//转化s分类
		Map<Long,Map<Integer,List<SclassifyNode>>> rootLevelNodes = new HashMap<Long, Map<Integer,List<SclassifyNode>>>();
		sclassifyResource.setRoot_Level_Nodes(rootLevelNodes);
		for (Entry<String, List<OecSclassifyModelBean>> entry : rootSclassifyModelBeanMap.entrySet()) {
			for (OecSclassifyModelBean oecSclassifyModelBean : entry.getValue()) {
				SclassifyNode sclassifyNode=transOecSclassifyModelBeanToSclassifyNode(oecSclassifyModelBean, entry.getKey());
				if(!rootLevelNodes.containsKey(sclassifyNode.getTop_tree_id())){
					rootLevelNodes.put(sclassifyNode.getTop_tree_id(),new HashMap<Integer,List<SclassifyNode>>());
				}
				Map<Integer,List<SclassifyNode>> treeNodeMap=rootLevelNodes.get(sclassifyNode.getTop_tree_id());
				if(!treeNodeMap.containsKey(sclassifyNode.getLevel())){
					treeNodeMap.put(sclassifyNode.getLevel(), new ArrayList<SclassifyNode>());
				}
				treeNodeMap.get(sclassifyNode.getLevel()).add(sclassifyNode);
			}
		}
	}

	private SclassifyNode transOecSclassifyModelBeanToSclassifyNode(OecSclassifyModelBean oecSclassifyModelBean,
			String topTreeId) {
		SclassifyNode sclassifyNode=new SclassifyNode();
		sclassifyNode.setAlgId(1L*oecSclassifyModelBean.getAlgId());
		sclassifyNode.setId(Long.parseLong(oecSclassifyModelBean.getClasId()));
		sclassifyNode.setTreeId(Long.parseLong(oecSclassifyModelBean.getClasId()));
		sclassifyNode.setId_path(oecSclassifyModelBean.getClasIdPath());
		sclassifyNode.setName(oecSclassifyModelBean.getClasName());
		sclassifyNode.setName_path(null);
		sclassifyNode.setLevel(oecSclassifyModelBean.getClasLevel());
		sclassifyNode.setNote(oecSclassifyModelBean.getDescription()==null?"":oecSclassifyModelBean.getDescription());
		sclassifyNode.setParentId(Long.parseLong(oecSclassifyModelBean.getClasParentId()));
		sclassifyNode.setSort(oecSclassifyModelBean.getClasSort());
		sclassifyNode.setValidFlg(1);
		sclassifyNode.setTop_tree_id(Long.parseLong(topTreeId));
		sclassifyNode.setContent(null);
		sclassifyNode.setSonNameToId(null);
		sclassifyNode.setCorrectRate(0.0);
		sclassifyNode.setModelsList(null);
		List<SclassCorpus> corpusList=new ArrayList<SclassCorpus>();
		if(oecSclassifyModelBean.getCorpuss()!=null){
			for(OecSclassifyCorpusModelBean oecSclassifyCorpusModelBean:oecSclassifyModelBean.getCorpuss()){
				SclassCorpus sclassCorpus=new SclassCorpus();
				sclassCorpus.setId(null);
				sclassCorpus.setContent(oecSclassifyCorpusModelBean.getCorContent());
				sclassCorpus.setTreeId(Long.parseLong(oecSclassifyCorpusModelBean.getClasId()));
				corpusList.add(sclassCorpus);
			}
		}
		sclassifyNode.setCorpusList(corpusList);
		sclassifyNode.setCorpusTotal(1L*corpusList.size());
		return sclassifyNode;
	}

	// -------------------信息抽取------------------------------
	private void initExtractResource(ExtractResource extractResource, OecProjectModelBean oecProjectModelBean) {
		// 信息抽取
		Map<String, List<OecExtractModelBean>> rootExtractModelBeanMap = new HashMap<String, List<OecExtractModelBean>>();
		List<OecOntologyModelBean> oecOntologys = oecProjectModelBean.getOecOntologys();
		for (OecOntologyModelBean oecOntologyModelBean : oecOntologys) {
			if (oecOntologyModelBean.getOntoTypeId().equals(3)) {// 信息抽取
				List<OecExtractModelBean> oecExtractModelBeans = oecOntologyModelBean.getOecExtracts();
				for (OecExtractModelBean oecExtractModelBean : oecExtractModelBeans) {
					String[] idPath = oecExtractModelBean.getExtIdPath().split("->");
					if (!rootExtractModelBeanMap.containsKey(idPath[0])) {
						rootExtractModelBeanMap.put(idPath[0], new ArrayList<OecExtractModelBean>());
					}
					List<OecExtractModelBean> beans = rootExtractModelBeanMap.get(idPath[0]);
					if (oecExtractModelBean.getExtLevel() == 0) {
						beans.add(0, oecExtractModelBean);
					} else {
						beans.add(oecExtractModelBean);
					}
				}
			}
		}
		// 转换c分类
		Map<String, List<ExtractNode>> rootNodeList = new HashMap<String, List<ExtractNode>>();
		extractResource.setRootNodeList(rootNodeList);
		for (Entry<String, List<OecExtractModelBean>> entry : rootExtractModelBeanMap.entrySet()) {
			List<ExtractNode> extractNodes = new ArrayList<ExtractNode>();
			List<OecExtractModelBean> oecExtractModelBeans = entry.getValue();
			for (OecExtractModelBean oecExtractModelBean : oecExtractModelBeans) {
				extractNodes.add(transOecExtractModelBeanToExtractNode(oecExtractModelBean, entry.getKey()));
			}
			rootNodeList.put(entry.getKey(), extractNodes);
		}
	}

	/**
	 * 将OecExtractModelBean 转化为ExtractNode
	 * 
	 * @param oecExtractModelBean
	 * @param key
	 * @return
	 */
	private ExtractNode transOecExtractModelBeanToExtractNode(OecExtractModelBean oecExtractModelBean,
			String topTreeId) {
		ExtractNode extractNode = new ExtractNode();
		extractNode.setNodeId(oecExtractModelBean.getExtId());
		extractNode.setNodeIdPath(oecExtractModelBean.getExtIdPath());
		extractNode.setParentId(oecExtractModelBean.getExtParentId());
		extractNode.setNodeName(oecExtractModelBean.getExtName());
		extractNode.setNodeNamePath(null);
		extractNode.setLevel(oecExtractModelBean.getExtLevel());
		extractNode.setSort(oecExtractModelBean.getExtSort());
		extractNode.setSplitFlg(0);
		extractNode.setSplitSymbol("");
		extractNode.setNote(oecExtractModelBean.getDescription() == null ? "" : oecExtractModelBean.getDescription());
		extractNode.setTopTreeId(topTreeId);
		List<ExtractInfo> extractInfos = new ArrayList<ExtractInfo>();
		/*
		 * for(OecExtractResourceModelBean
		 * oecExtractResourceModelBean:oecExtractModelBean.getResources()){
		 * ExtractInfo extractInfo=new ExtractInfo();
		 * extractInfo.setIdExp(idExp); extractInfo.setNameExp(nameExp);
		 * extractInfo.setFactor(5);
		 * extractInfo.setLanguageId(getLangueId(""+oecExtractResourceModelBean.
		 * getLangueId()));
		 * extractInfo.setValidFlg(oecExtractResourceModelBean.getValidFlg());
		 * extractInfo.setSplitFlg(0); extractInfo.setSplitSymbol("");
		 * extractInfo.setMatchedInfo(null); extractInfos.add(extractInfo); }
		 */
		extractNode.setExtractInfo(extractInfos);
		return extractNode;
	}

	// --------------------多元组------------------------------
	private void initMultitupleResource(MultitupleResource multitupleResource,
			OecProjectModelBean oecProjectModelBean) {
		Map<String, MultiTupEntity> mulTupMap = new HashMap<String, MultiTupEntity>();
		Map<String, List<TupleInfo>> tupleInfoMap = new HashMap<String, List<TupleInfo>>();
		Map<String, List<TupMemRelEntity>> memRelateMap = new HashMap<String, List<TupMemRelEntity>>();
		multitupleResource.setMulTupMap(mulTupMap);
		multitupleResource.setTupleInfoMap(tupleInfoMap);
		multitupleResource.setMemRelateMap(memRelateMap);
		// 多元组
		List<OecOntologyModelBean> oecOntologys = oecProjectModelBean.getOecOntologys();
		for (OecOntologyModelBean oecOntologyModelBean : oecOntologys) {
			if (oecOntologyModelBean.getOntoTypeId().equals(4)) {// 多元组
				List<OecMultiTupleModelBean> oecMultiTuples = oecOntologyModelBean.getOecMultiTuples();
				for (OecMultiTupleModelBean oecMultiTupleModelBean : oecMultiTuples) {
					transOecMultiTupleModelBeanToMultitupleResource(oecMultiTupleModelBean, multitupleResource);
				}
			}
		}
	}

	private void transOecMultiTupleModelBeanToMultitupleResource(OecMultiTupleModelBean oecMultiTupleModelBean,
			MultitupleResource multitupleResource) {
		Map<String, MultiTupEntity> mulTupMap = multitupleResource.getMulTupMap();
		Map<String, List<TupleInfo>> tupleInfoMap = multitupleResource.getTupleInfoMap();
		Map<String, List<TupMemRelEntity>> memRelateMap = multitupleResource.getMemRelateMap();
		// 多元组
		String multiTupleId=oecMultiTupleModelBean.getMultiTupId();
		MultiTupEntity multiTupEntity = new MultiTupEntity();
		multiTupEntity.setId(Long.parseLong(multiTupleId));
		multiTupEntity.setName(oecMultiTupleModelBean.getMultiTupName());
		multiTupEntity.setNote(
				oecMultiTupleModelBean.getDescription() == null ? "" : oecMultiTupleModelBean.getDescription());
		multiTupEntity.setSplitFlg(0);
		multiTupEntity.setSplitSymbol("");
		multiTupEntity.setSynFlg(oecMultiTupleModelBean.getSyhType());
		multiTupEntity.setSynResExp(oecMultiTupleModelBean.getSyhResourceExpression());
		multiTupEntity.setTupTotal(oecMultiTupleModelBean.getTupTotal());
		multiTupEntity.setModelId(null);
		mulTupMap.put(multiTupleId, multiTupEntity);
		// 元组
		List<TupleInfo> tupleInfos = new ArrayList<TupleInfo>();
		for (OecTupleModelBean oecTupleModelBean : oecMultiTupleModelBean.getOecTuples()) {
			TupleInfo tupleInfo = new TupleInfo();
			tupleInfo.setId(oecTupleModelBean.getTupId());
			tupleInfo.setName(oecTupleModelBean.getTupName());
			tupleInfo.setMemnum(oecTupleModelBean.getTupMemNum());
			tupleInfo.setSort(oecTupleModelBean.getTupSort());
			tupleInfo.setType(oecTupleModelBean.getTupType());
			List<TupMemEntity> memList = new ArrayList<TupMemEntity>();
			for (OecTupleMemberModelBean oecTupleMemberModelBean : oecTupleModelBean.getTupMembers()) {
				TupMemEntity tupMemEntity = new TupMemEntity();
				tupMemEntity.setId(Long.parseLong(oecTupleMemberModelBean.getMemId()));
				tupMemEntity.setName(oecTupleMemberModelBean.getMemName());
				tupMemEntity.setEcnType(oecTupleMemberModelBean.getEcnType());
				tupMemEntity.setEcnId(Long.parseLong(oecTupleMemberModelBean.getEcnId()));
				tupMemEntity.setTupId(Long.parseLong(oecTupleMemberModelBean.getTupId()));
				memList.add(tupMemEntity);
			}
			tupleInfo.setMemList(memList);
			tupleInfos.add(tupleInfo);
		}
		tupleInfoMap.put(multiTupleId, tupleInfos);
		// 元组成员关系
		List<TupMemRelEntity> tupMemRelEntities = new ArrayList<TupMemRelEntity>();
		for (OecTupleMemberRelateModelBean oecTupleMemberRelateModelBean : oecMultiTupleModelBean.getMemRelates()) {
			TupMemRelEntity tupMemRelEntity = new TupMemRelEntity();
			tupMemRelEntity.setID(null);
			tupMemRelEntity.setOriginTupMemId(Long.parseLong(oecTupleMemberRelateModelBean.getOriginTupMemId()));
			tupMemRelEntity.setTargetTupMemId(Long.parseLong(oecTupleMemberRelateModelBean.getTargetTupMemId()));
			tupMemRelEntity.setEmotValue(oecTupleMemberRelateModelBean.getEmotValue());
			tupMemRelEntity.setSplitFlg(0);
			tupMemRelEntity.setSplitSymbol("");
			tupMemRelEntity.setSynExp(oecMultiTupleModelBean.getSyhResourceExpression());
			tupMemRelEntities.add(tupMemRelEntity);
		}
		memRelateMap.put(multiTupleId, tupMemRelEntities);
	}
	// -------------------公用方法-----------------------

	public static String getLangueId(String oldId) {
		if (oldId == null || oldId.equals("1001")) {
			return "1";
		} else {
			return "2";
		}
	}
}
