package eki.ekilex.data;

import java.util.List;

import javax.persistence.Column;

import eki.common.data.AbstractDataObject;

public class Meaning extends AbstractDataObject {

	private static final long serialVersionUID = 1L;

	@Column(name = "meaning_id")
	private Long meaningId;

	@Column(name = "lexeme_ids")
	private List<Long> lexemeIds;

	private List<DefinitionLangGroup> definitionLangGroups;

	private List<LexemeLangGroup> lexemeLangGroups;

	private List<OrderedClassifier> domains;

	private List<Classifier> semanticTypes;

	private List<FreeForm> freeforms;

	private List<FreeForm> learnerComments;

	private List<Image> images;

	private List<Note> publicNotes;

	private List<Relation> relations;

	private List<List<Relation>> groupedRelations;

	public Long getMeaningId() {
		return meaningId;
	}

	public void setMeaningId(Long meaningId) {
		this.meaningId = meaningId;
	}

	public List<Long> getLexemeIds() {
		return lexemeIds;
	}

	public void setLexemeIds(List<Long> lexemeIds) {
		this.lexemeIds = lexemeIds;
	}

	public List<DefinitionLangGroup> getDefinitionLangGroups() {
		return definitionLangGroups;
	}

	public void setDefinitionLangGroups(List<DefinitionLangGroup> definitionLangGroups) {
		this.definitionLangGroups = definitionLangGroups;
	}

	public List<LexemeLangGroup> getLexemeLangGroups() {
		return lexemeLangGroups;
	}

	public void setLexemeLangGroups(List<LexemeLangGroup> lexemeLangGroups) {
		this.lexemeLangGroups = lexemeLangGroups;
	}

	public List<OrderedClassifier> getDomains() {
		return domains;
	}

	public void setDomains(List<OrderedClassifier> domains) {
		this.domains = domains;
	}

	public List<Classifier> getSemanticTypes() {
		return semanticTypes;
	}

	public void setSemanticTypes(List<Classifier> semanticTypes) {
		this.semanticTypes = semanticTypes;
	}

	public List<FreeForm> getFreeforms() {
		return freeforms;
	}

	public void setFreeforms(List<FreeForm> freeforms) {
		this.freeforms = freeforms;
	}

	public List<FreeForm> getLearnerComments() {
		return learnerComments;
	}

	public void setLearnerComments(List<FreeForm> learnerComments) {
		this.learnerComments = learnerComments;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public List<Note> getPublicNotes() {
		return publicNotes;
	}

	public void setPublicNotes(List<Note> publicNotes) {
		this.publicNotes = publicNotes;
	}

	public List<Relation> getRelations() {
		return relations;
	}

	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}

	public List<List<Relation>> getGroupedRelations() {
		return groupedRelations;
	}

	public void setGroupedRelations(List<List<Relation>> groupedRelations) {
		this.groupedRelations = groupedRelations;
	}

}
