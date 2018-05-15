package com.dinfo.oec.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;

import com.dinfo.log.Loggers;
import com.dinfo.model.model.Model;
import com.dinfo.model.model.SclassCorpus;
import com.dinfo.model.model.SclassModel;
import com.dinfo.model.onto.bean.BaseResource;
import com.dinfo.model.onto.bean.CclassifyResource;
import com.dinfo.model.onto.bean.ExtractInfo;
import com.dinfo.model.onto.bean.ExtractResource;
import com.dinfo.model.onto.bean.MultitupleResource;
import com.dinfo.model.onto.bean.RelationResource;
import com.dinfo.model.onto.bean.SclassifyResource;
import com.dinfo.model.onto.dto.CclassifyExpression;
import com.dinfo.model.onto.dto.CclassifyNode;
import com.dinfo.model.onto.dto.ConceptNode;
import com.dinfo.model.onto.dto.ConceptNodeValue;
import com.dinfo.model.onto.dto.ElementNode;
import com.dinfo.model.onto.dto.ElementNodeValue;
import com.dinfo.model.onto.dto.ExtractNode;
import com.dinfo.model.onto.dto.RelateExpression;
import com.dinfo.model.onto.dto.RelateNode;
import com.dinfo.model.onto.dto.SclassifyNode;
import com.dinfo.model.onto.dto.TupleInfo;
import com.dinfo.model.onto.model.MultiTupEntity;
import com.dinfo.model.onto.model.TupMemEntity;
import com.dinfo.model.onto.model.TupMemRelEntity;
import com.dinfo.model.utils.ModelGuavaCache;


public class ModelXmlUtilsOEC610 {

	private static Logger logger = Loggers.get(ModelXmlUtilsOEC610.class);

	//=======================生成xml===============
	public  String parseToXml(Model model,String modelKey) throws ExecutionException {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("utf-8");
		Element schema=doc.addElement("model");
		schema.addAttribute("id", model.getId()+"");
		schema.addAttribute("name", model.getName());
		schema.addAttribute("appId", model.getAppId()+"");
		schema.addAttribute("status", model.getStatus()+"");
		schema.addAttribute("note", model.getNote());
		
		Element ontology=schema.addElement("ontology");
		//String key = model.getId() + "-release";
		// c分类资源获取及转换
		//CclassifyResource cclassifyResource = MultiModelResource.cModelResourceMap.get(key);
		//从本地缓存中取
		CclassifyResource cclassifyResource = ModelGuavaCache.cPublicCahceBuilder.get(modelKey);
		
		if (cclassifyResource != null) {
			Map<String, List<CclassifyNode>> treeNodeListMap = cclassifyResource
					.getTree_nodelist();
			if(treeNodeListMap!=null){
				for (Map.Entry<String,List<CclassifyNode>> entry: treeNodeListMap.entrySet()) {
					createCcOntologyTree(ontology,entry.getValue());
				}
			}
		} else {
			logger.error("parseToXml cclassifyResource为null");
		}
		// S分类节点及其表达式
		SclassifyResource sclassifyResource = ModelGuavaCache.sPublicCahceBuilder.get(modelKey);
		if (sclassifyResource != null) {
			Map<Long, Map<Integer, List<SclassifyNode>>> root_Level_Nodes = sclassifyResource
					.getRoot_Level_Nodes();
			if(root_Level_Nodes!=null){
				for (Map.Entry<Long,Map<Integer,List<SclassifyNode>>> entry : root_Level_Nodes.entrySet()) {
					createScOntologyTree(ontology,entry.getValue());
				}
			}
		} else {
			logger.error("parseToXml sclassifyResource为null");
		}
		// 信息抽取
		ExtractResource extractResource = ModelGuavaCache.ePublicCahceBuilder.get(modelKey);
		if(extractResource!=null){
			Map<String, List<ExtractNode>> rootNodeList = extractResource.getRootNodeList();
			if(rootNodeList!=null){
				for (Map.Entry<String,List<ExtractNode>> entry : rootNodeList.entrySet()) {
					createExOntologyTree(ontology,entry.getValue());
				}
			}
		}else{
			logger.error("parseToXml extractResource为null");
		}
		// 关联关系
		RelationResource relationResource = ModelGuavaCache.rPublicCahceBuilder.get(modelKey);
		if(relationResource!=null){
			Map<String, List<RelateNode>> rootRelNodeMap = relationResource.getRootRelNodeMap();
			if(rootRelNodeMap!=null){
				for (Map.Entry<String,List<RelateNode>> entry : rootRelNodeMap.entrySet()) {
					createRrOntologyTree(ontology, entry.getValue());
				}
			}
		}else{
			logger.error("parseToXml relationResource为null");
		}
		// 多元组
		MultitupleResource multitupleResource = ModelGuavaCache.mPublicCahceBuilder.get(modelKey);
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
					createRmOntologyTree(ontology,multiTupEntity,tupleInfos,tupMemRelEntities);
				}
			}
		}else{
			logger.error("parseToXml multitupleResource为null");
		}
		// 获取要素、概念资源
		BaseResource baseResource = ModelGuavaCache.basePublicCahceBuilder.get(modelKey);
		if (baseResource != null) {
			//要素
			Map<String, List<ElementNode>> tree_elelist = baseResource
					.getTree_elelist();
			if(tree_elelist!=null && tree_elelist.size()>0){
				Element element=schema.addElement("element");
				for (Map.Entry<String,List<ElementNode>> entry : tree_elelist.entrySet()) {
					createElementTree(element,entry.getValue(),entry.getKey());
				}
			}
			//概念
			Map<String, List<ConceptNode>> tree_conlist = baseResource
					.getTree_conlist();
			if(tree_conlist!=null &&  tree_conlist.size()>0){
				Element concept=schema.addElement("concept");
				for (Map.Entry<String,List<ConceptNode>> entry : tree_conlist.entrySet()) {
					createConceptTree(concept,entry.getValue(),entry.getKey());
				}
			}
		} else {
			logger.error("parseToXml baseResource为null");
		}
		return doc.asXML();
	}
	
	//创建c分类本体树
	public static void createCcOntologyTree(Element ontology,List<CclassifyNode> cclassifyNodes){
		//为空时返回
		if(cclassifyNodes==null || cclassifyNodes.size()==0){
			return;
		}
		Element ontologyTree=ontology.addElement("ontologyTree");
		ontologyTree.addAttribute("type", "cc");
		for (CclassifyNode cclassifyNode : cclassifyNodes) {
			if(cclassifyNode.getId().equals(cclassifyNode.getTopTreeId())){//根节点
				ontologyTree.addAttribute("id", cclassifyNode.getTopTreeId());
				ontologyTree.addAttribute("synFlg", cclassifyNode.getSyn_flg()+"");
				ontologyTree.addAttribute("synResExp",cclassifyNode.getSyn_exp());
				ontologyTree.addAttribute("splitFlg", cclassifyNode.getSplit_flg()+"");
				ontologyTree.addAttribute("splitSymbol", cclassifyNode.getSplit_symbol()+"");
			}
			//节点属性
			Element node=ontologyTree.addElement("node");
			node.addAttribute("value", "0");
			node.addElement("id").addText(cclassifyNode.getId());
			node.addElement("idPath").addText(cclassifyNode.getId_path());
			node.addElement("name").addText(cclassifyNode.getName());
			node.addElement("level").addText(cclassifyNode.getLevel()+"");
			node.addElement("parentId").addText(cclassifyNode.getParentId());
			node.addElement("sort").addText(cclassifyNode.getSort()+"");
			node.addElement("weight").addText(cclassifyNode.getWeight()+"");
			node.addElement("description").addText(cclassifyNode.getNote()+"");
			//表达式
			List<CclassifyExpression> exp_list = cclassifyNode.getExp_list();
			if(exp_list!=null && exp_list.size()>0){
				Element expressions=node.addElement("expressions");
				for (CclassifyExpression cclassifyExpression : exp_list) {
					Element expression=expressions.addElement("expression");
					expression.setText(cclassifyExpression.getNameExp().trim());
					expression.addAttribute("id", cclassifyExpression.getId());
					expression.addAttribute("ecidExp", cclassifyExpression.getIdExp().trim());
					expression.addAttribute("langueId", cclassifyExpression.getLanguageId()==null?"1":cclassifyExpression.getLanguageId());
					expression.addAttribute("factor", cclassifyExpression.getFactor()+"");
					expression.addAttribute("identifyFlg", cclassifyExpression.getIdentFlg()+"");
					expression.addAttribute("validFlg", cclassifyExpression.getValidFlg()+"");
					expression.addAttribute("synExp", cclassifyExpression.getSynAnalyExp());
					expression.addAttribute("splitFlg", cclassifyExpression.getSplitFlg()+"");
					expression.addAttribute("splitSymbol", cclassifyExpression.getSplitSymbol());
				}
			}
		}
		
	}
	
	//创建s分类本体树
	public static void createScOntologyTree(Element ontology,Map<Integer,List<SclassifyNode>> sclassifyNodeMap){
		//为空时返回
		if(sclassifyNodeMap==null || sclassifyNodeMap.size()==0){
			return;
		}
		Element ontologyTree=ontology.addElement("ontologyTree");
		ontologyTree.addAttribute("type", "sc");
		for (Map.Entry<Integer,List<SclassifyNode>> entry : sclassifyNodeMap.entrySet()) {
			List<SclassifyNode> sclassifyNodes = entry.getValue();
			for (SclassifyNode sclassifyNode : sclassifyNodes) {
				if(sclassifyNode.getId().equals(sclassifyNode.getTop_tree_id())){//根节点
					ontologyTree.addAttribute("id", sclassifyNode.getTop_tree_id()+"");
					ontologyTree.addAttribute("algId", sclassifyNode.getAlgId()+"");
				}
				//节点属性
				Element node=ontologyTree.addElement("node");
				node.addAttribute("value", "0");
				node.addElement("id").addText(sclassifyNode.getId()+"");
				node.addElement("idPath").addText(sclassifyNode.getId_path());
				node.addElement("name").addText(sclassifyNode.getName());
				node.addElement("level").addText(sclassifyNode.getLevel()+"");
				node.addElement("parentId").addText(sclassifyNode.getParentId()+"");
				node.addElement("sort").addText(sclassifyNode.getSort()+"");
				node.addElement("description").addText(sclassifyNode.getNote()+"");
				//模型
				List<SclassModel> modelList = sclassifyNode.getModelsList();
				if(modelList!=null && modelList.size()>0){
					Element models=node.addElement("models");
					for (SclassModel sclassModel : modelList) {
						Element model=models.addElement("model");
						model.addAttribute("correctRate", sclassModel.getCorrectRate()+"");
						model.addAttribute("corpusTotal", sclassModel.getCorpusTotal()+"");
						model.addAttribute("validFlg", sclassModel.getValidFlg()+"");
						model.setText(sclassModel.getContent());
					}
				}
				//语料
				List<SclassCorpus> corpusList = sclassifyNode.getCorpusList();
				if(corpusList!=null && corpusList.size()>0){
					Element corpusListE=node.addElement("corpusList");
					for (SclassCorpus sclassCorpus : corpusList) {
						corpusListE.addElement("corpus").setText(sclassCorpus.getContent());
					}
				}
			}
			
		}
		
	}

	//创建抽取本体树
	public static void createExOntologyTree(Element ontology,List<ExtractNode> extractNodes){
		//为空时返回
		if(extractNodes==null || extractNodes.size()==0){
			return;
		}
		Element ontologyTree=ontology.addElement("ontologyTree");
		ontologyTree.addAttribute("type", "ex");
		for (ExtractNode extractNode : extractNodes) {
			if(extractNode.getNodeId().equals(extractNode.getTopTreeId())){//根节点
				ontologyTree.addAttribute("id", extractNode.getTopTreeId());
				ontologyTree.addAttribute("splitFlg", extractNode.getSplitFlg()+"");
				ontologyTree.addAttribute("splitSymbol", extractNode.getSplitSymbol());
			}
			//节点属性
			Element node=ontologyTree.addElement("node");
			node.addAttribute("value", "0");
			node.addElement("id").addText(extractNode.getNodeId());
			node.addElement("idPath").addText(extractNode.getNodeIdPath());
			node.addElement("name").addText(extractNode.getNodeName());
			node.addElement("level").addText(extractNode.getLevel()+"");
			node.addElement("parentId").addText(extractNode.getParentId());
			node.addElement("sort").addText(extractNode.getSort()+"");
			node.addElement("description").addText(extractNode.getNote()+"");
			//表达式
			List<ExtractInfo> exp_list = extractNode.getExtractInfo();
			if(exp_list!=null && exp_list.size()>0){
				Element expressions=node.addElement("expressions");
				for (ExtractInfo extractInfo : exp_list) {
					Element expression=expressions.addElement("expression").addText(extractInfo.getNameExp().trim());
					expression.addAttribute("ecidExp", extractInfo.getIdExp().trim());
					expression.addAttribute("langueId", extractInfo.getLanguageId());
					expression.addAttribute("factor", extractInfo.getFactor()+"");
					expression.addAttribute("validFlg", extractInfo.getValidFlg()+"");
					expression.addAttribute("splitFlg", extractInfo.getSplitFlg()+"");
					expression.addAttribute("splitSymbol", extractInfo.getSplitSymbol()+"");
				}
			}
		}
	}

	//创建关联关系本体树
	public static void createRrOntologyTree(Element ontology,List<RelateNode> relateNodes){
		//为空时返回
		if(relateNodes==null || relateNodes.size()==0){
			return;
		}
		Element ontologyTree=ontology.addElement("ontologyTree");
		ontologyTree.addAttribute("type", "rr");
		for (RelateNode relateNode : relateNodes) {
			if(relateNode.getId().equals(relateNode.getTopTreeId())){//根节点
				ontologyTree.addAttribute("id", relateNode.getTopTreeId());
				ontologyTree.addAttribute("synFlg", relateNode.getSynFlg()+"");
				ontologyTree.addAttribute("synResExp",relateNode.getSynExp());
				ontologyTree.addAttribute("splitFlg", relateNode.getSplitFlg()+"");
				ontologyTree.addAttribute("splitSymbol", relateNode.getSplitSymbol()+"");
			}
			//节点属性
			Element node=ontologyTree.addElement("node");
			node.addAttribute("value", "0");
			node.addElement("id").addText(relateNode.getId());
			node.addElement("idPath").addText(relateNode.getIdPath());
			node.addElement("name").addText(relateNode.getName());
			node.addElement("level").addText(relateNode.getLevel()+"");
			node.addElement("parentId").addText(relateNode.getParentId());
			node.addElement("sort").addText(relateNode.getSort()+"");
			node.addElement("description").addText(relateNode.getNote()+"");
			//表达式
			List<RelateExpression> exp_list = relateNode.getRelExpList();
			if(exp_list!=null && exp_list.size()>0){
				Element expressions=node.addElement("expressions");
				for (RelateExpression relateExpression : exp_list) {
					Element expression=expressions.addElement("expression").addText(relateExpression.getNameExp().trim());
					expression.addAttribute("ecidExp", relateExpression.getIdExp().trim());
					expression.addAttribute("factor", relateExpression.getFactor()+"");
					expression.addAttribute("identifyFlg", relateExpression.getIdentifyFlg()+"");
					expression.addAttribute("validFlg", relateExpression.getValidFlg()+"");
					expression.addAttribute("synExp", relateExpression.getSynExp());
					expression.addAttribute("splitFlg", relateExpression.getSplitFlg()+"");
					expression.addAttribute("splitSymbol", relateExpression.getSplitSymbol());
				}
			}
		}
	}

	//创建多元组本体树
	public static void createRmOntologyTree(Element ontology,MultiTupEntity mulTupEntity,List<TupleInfo> tupleInfos,List<TupMemRelEntity> tupMemRelEntities){
		//为空时返回
		if(mulTupEntity==null){
			return;
		}
		Element ontologyTree=ontology.addElement("ontologyTree");
		ontologyTree.addAttribute("type", "rm");
		ontologyTree.addAttribute("id", mulTupEntity.getId()+"");
		ontologyTree.addAttribute("synFlg", mulTupEntity.getSynFlg()+"");
		ontologyTree.addAttribute("synResExp", mulTupEntity.getSynResExp());
		ontologyTree.addAttribute("splitFlg", mulTupEntity.getSplitFlg()+"");
		ontologyTree.addAttribute("splitSymbol", mulTupEntity.getSplitSymbol()+"");
		//多元组
		Element node=ontologyTree.addElement("node");
		node.addAttribute("value", "0");
		node.addElement("id").addText(mulTupEntity.getId()+"");
		node.addElement("name").addText(mulTupEntity.getName());
		node.addElement("description").addText(mulTupEntity.getNote()+"");
		//元组
		if(tupleInfos!=null && tupleInfos.size()>0){
			Element tuples=node.addElement("tuples");
			tuples.addAttribute("tup_total", mulTupEntity.getTupTotal()+"");
			for (TupleInfo tupleInfo : tupleInfos) {
				Element tuple=tuples.addElement("tuple");
				tuple.addAttribute("id", tupleInfo.getId());
				tuple.addAttribute("sort", tupleInfo.getSort()+"");
				tuple.addAttribute("mem_num", tupleInfo.getMemnum()==null?"0":tupleInfo.getMemnum()+"");
				tuple.addAttribute("type", tupleInfo.getType()+"");
				tuple.addElement("name").addText(tupleInfo.getName()==null?"":tupleInfo.getName());
				List<TupMemEntity> memList = tupleInfo.getMemList();
				if(memList!=null && memList.size()>0){
					Element members=tuple.addElement("members");
					for (TupMemEntity tupMemEntity : memList) {
						Element member=members.addElement("member");
						member.addText(tupMemEntity.getName());
						member.addAttribute("id", tupMemEntity.getId()+"");
						member.addAttribute("impResId", tupMemEntity.getEcnId()+"");
						member.addAttribute("impResType", tupMemEntity.getEcnType()+"");
					}
				}
			}
		}
		//成员关系
		if(tupMemRelEntities!=null && tupMemRelEntities.size()>0){
			Element relates=node.addElement("relates");
			for (TupMemRelEntity tupMemRelEntity : tupMemRelEntities) {
				Element relate=relates.addElement("relate");
				relate.addAttribute("id", tupMemRelEntity.getID()+"");
				relate.addAttribute("synExp", tupMemRelEntity.getSynExp());
				relate.addAttribute("splitFlg", tupMemRelEntity.getSplitFlg()+"");
				relate.addAttribute("splitSymbol", tupMemRelEntity.getSplitSymbol()+"");
				relate.addAttribute("emotion_value", tupMemRelEntity.getEmotValue()+"");
				relate.addElement("srcmem").addText(tupMemRelEntity.getOriginTupMemId()+"");
				relate.addElement("desmem").addText(tupMemRelEntity.getTargetTupMemId()+"");
			}
		}
	}
	
	//创建要素树
	public static void createElementTree(Element element,List<ElementNode> elementNodes,String topTreeId){
		//为空时返回
		if(elementNodes==null || elementNodes.size()==0){
			return;
		}
		Element elementTree=element.addElement("elementTree");
		elementTree.addAttribute("id", topTreeId);
		for (ElementNode elementNode : elementNodes) {
			//要素节点
			Element node=elementTree.addElement("node");
			node.addAttribute("value", "0");
			node.addElement("id").addText(elementNode.getId());
			node.addElement("idPath").addText(elementNode.getId_path());
			node.addElement("name").addText(elementNode.getName());
			node.addElement("level").addText(elementNode.getLevel()+"");
			node.addElement("parentId").addText(elementNode.getParentId());
			node.addElement("sort").addText(elementNode.getSort()+"");
			node.addElement("weight").addText(elementNode.getWeight()+"");
			node.addElement("langueId").addText(elementNode.getLangueId());
			node.addElement("description").addText(elementNode.getNote()+"");
			List<ElementNodeValue> valueList = elementNode.getValueList();
			//要素值
			if(valueList!=null && valueList.size()>0){
				Element expressions=node.addElement("expressions");
				for (ElementNodeValue elementNodeValue : valueList) {
					Element expression=expressions.addElement("expression");
					expression.addAttribute("type", elementNodeValue.getType()+"");
					expression.addAttribute("validFlg",elementNodeValue.getValidFlg()+"");
					expression.addText(elementNodeValue.getValue());
				}
			}
		}
	}
	
	//创建概念树
	public static void createConceptTree(Element concept,List<ConceptNode> conceptNodes,String topTreeId){
		//为空时返回
		if(conceptNodes==null || conceptNodes.size()==0){
			return;
		}
		Element conceptTree=concept.addElement("conceptTree");
		conceptTree.addAttribute("id", topTreeId);
		for (ConceptNode conceptNode : conceptNodes) {
			//概念节点
			Element node=conceptTree.addElement("node");
			node.addAttribute("value", "0");
			node.addElement("id").addText(conceptNode.getId());
			node.addElement("idPath").addText(conceptNode.getId_path());
			node.addElement("name").addText(conceptNode.getName());
			node.addElement("level").addText(conceptNode.getLevel()+"");
			node.addElement("parentId").addText(conceptNode.getParentId());
			node.addElement("sort").addText(conceptNode.getSort()+"");
			node.addElement("weight").addText(conceptNode.getWeight()+"");
			node.addElement("langueId").addText(conceptNode.getLangueId());
			node.addElement("description").addText(conceptNode.getNote());
			List<ConceptNodeValue> valueList = conceptNode.getValueList();
			//概念值
			if(valueList!=null && valueList.size()>0){
				Element expressions=node.addElement("expressions");
				for (ConceptNodeValue conceptNodeValue : valueList) {
					Element expression=expressions.addElement("expression");
					expression.addAttribute("type", conceptNodeValue.getType()+"");
					expression.addAttribute("validFlg", conceptNodeValue.getValidFlg()+"");
					expression.addText(conceptNodeValue.getValue());
				}
			}
		}
	}
}
