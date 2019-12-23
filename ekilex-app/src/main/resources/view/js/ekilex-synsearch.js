function initialise() {
	var IS_KEYBOARD_MODE = false;
	var NAVIGATE_SELECTED_CLASS = 'keyboard-nav-list-item-active';
	var NAVIGATE_DECLINED_CLASS = 'keyboard-nav-declined-item';
	var NAVIGATE_SELECTED_ATTR = 'data-navigate-selected';
	let activeSearchResultID;

	//Enter keyboard edit mode
	$(document).on("click", "#keyboardEditBtn", function() {
		IS_KEYBOARD_MODE = true;
		console.log('IS_KEYBOARD_MODE: ' + IS_KEYBOARD_MODE);
		$('body').addClass('keyboard-edit-mode-active');
		//TODO refactor
		$('.keyboard-nav-list').each(function(e) {
			$(this).removeAttr('data-active-panel').removeClass('keyboard-nav-list-active');
		});
		activateList(3);

		$(this).attr('disabled', true);

	});

	function activateList(listIndx, itemIndx = 0) {
		let activatedList = $('div[data-panel-index="' + listIndx + '"]');
		activatedList.attr('data-active-panel', true).addClass('keyboard-nav-list-active');
		itemToSelect = activatedList.find('[data-navigate-selected="true"]').length ? activatedList.find('[data-navigate-selected="true"]') : activatedList.find('[data-navigate-index="' + itemIndx + '"]');
		itemToSelect.addClass('keyboard-nav-list-item-active');
		itemToSelect.attr(NAVIGATE_SELECTED_ATTR, true);
		changeSynonymDefinitionDisplay('show');
	}

	function changeLexemeMeaningOrdering(target, delta) {
		let orderBlock = target.closest('.orderable');
		let opCode = orderBlock.attr("data-op-code");
		let itemToMove = target.closest('[data-orderby]');
		let additionalInfo = orderBlock.attr('data-additional-info');
		let items = orderBlock.find('[data-orderby]');
		let itemToMovePos = items.index(itemToMove);
		let orderedItems = [];
		if (itemToMovePos + delta >= 0 && itemToMovePos + delta < items.length) {
			let orderby = $(items.get(itemToMovePos + delta)).attr('data-orderby');
			let orderpos = $(items.get(itemToMovePos + delta)).attr('data-orderpos');

			let increment = delta > 0 ? -1 : 1;

			for (var position = itemToMovePos + delta; (delta < 0 && position < itemToMovePos) || (delta >= 0 && position > itemToMovePos) ; position += increment) {
				let nextPos = delta > 0 ? position -1 : position + 1;
				let nextOrderPos = $(items.get(nextPos)).attr('data-orderpos');

				$(items.get(position)).attr('data-orderby', $(items.get(nextOrderPos)).attr('data-orderby'));
				$(items.get(position)).attr('data-orderpos', nextOrderPos);
			}

			$(items.get(itemToMovePos)).attr('data-orderpos', orderpos);
			$(items.get(itemToMovePos)).attr('data-orderby', orderby);

			if (delta > 0) {
				$(items.get(itemToMovePos + delta)).after($(items.get(itemToMovePos)));
			} else {
				$(items.get(itemToMovePos + delta)).before($(items.get(itemToMovePos)));
			}
			items = orderBlock.find('[data-orderby]');
			items.each(function(indx, item) {
				$(item).find('.order-up').prop('hidden', indx == 0);
				$(item).find('.order-down').prop('hidden', indx == items.length - 1);
				let itemData = {};
				itemData.id = $(item).attr('data-id');
				itemData.code = $(item).attr('data-code');
				itemData.orderby = $(item).attr('data-orderby');
				orderedItems.push(itemData);
			});
		}
		return {
			opCode: opCode,
			items: orderedItems,
			additionalInfo: additionalInfo
		};
	}

	$(document).on("click", ".popover-close-btn", function() {
		$(this).parents(".popover").popover('hide');
	});

	$(document).on("click", "#synLayerCompleteBtn", function() {

		let wordId = $(this).data('word-id');
		let actionUrl = applicationUrl + "syn_layer_complete/" + wordId;
		let callbackFunc = () => refreshDetails();
		doPostRelationChange(actionUrl, callbackFunc);
	});

	$(document).on("click", ":button[name='synDetailsBtn']", function() {

		var savedScrollPositions = getScrollPositions();
		let id = $(this).data('id');

		let markedSynWordId = $(document).find('.keyboard-nav-list-item-selected').children(':first').data('word-id');
		$('#synSearchResultsDiv').find('.list-group-item').each(function() {
			$(this).removeClass('keyboard-nav-list-item-active active');
		});
		$('#synSearchResultsDiv').find('[data-navigate-selected]').removeAttr('data-navigate-selected');

		$(this).parent().addClass('active');
		$(this).parent().attr('data-navigate-selected', true);

		$("[id^='syn_select_wait_']").hide();
		$("#syn_select_wait_" + id).show();
		openWaitDlg();
		let detailsUrl = applicationUrl + 'syn_worddetails/' + id;
		if (markedSynWordId != undefined) {
			detailsUrl += '?markedSynWordId=' + markedSynWordId;
		}

		$.get(detailsUrl).done(function(data) {
			let detailsDiv = $('#syn_details_div');
			detailsDiv.replaceWith(data);
			closeWaitDlg();
			$("#syn_select_wait_" + id).hide();
			$('[data-toggle="tooltip"]').tooltip();

			$('.syn-stats-popover').popover({
				template:'<div class="popover popover-inverted synonym-statistics-popover" role="tooltip"><div class="arrow"></div><div class="popover-head"><h3 class="popover-header" ></h3><button type="button" class="bnt btn-sm btn-outline-light border-0  popover-close-btn"><i class="fa fa-close" aria-hidden="true"></i></button></div><div class="popover-body"></div></div>',
				placement:'top',
				content: function() {
					// Get the content from the hidden sibling.
					return $(this).siblings('.syn-stats-content').html();
				}
			});

			$(document).find('.draggable-synonym').draggable({
				revert: "invalid",
				appendTo: "body",
				containment: "window",
				helper: "clone"
			});
			$(document).find('.draggable-meaning').draggable(
				{
					revert: "invalid",
					appendTo: "body",
					containment: "window",
					helper: "clone"

				}
			);

			$(document).find('.droppable-meaning').droppable({
				accept: function(draggableDiv) {
					let draggableLexemeId = draggableDiv.closest('.droppable-lexeme').attr('data-lexeme-id');
					let droppableLexemeId =  $(this).closest('.droppable-lexeme').attr('data-lexeme-id');

					return draggableLexemeId === droppableLexemeId;
					}
				,
				greedy: true,
				classes: {
					"ui-droppable-active": "ui-state-active",
					"ui-droppable-hover": "ui-state-hover"
				},
				drop: function(event, ui) {
					let draggableOrderable = ui.draggable.closest('[data-orderpos]');
					let draggableOrderPos = Number(draggableOrderable.data('orderpos'));
					let orderingBtn = $(this);
					let droppableOrderable = $(this).closest('[data-orderpos]');
					let droppableOrderPos = Number(droppableOrderable.data('orderpos'));
					let posDelta = droppableOrderPos - draggableOrderPos;

					let draggableItem = ui.draggable;

					let orderingData = changeLexemeMeaningOrdering(draggableItem, posDelta);
					openWaitDlg();
					postJson(applicationUrl + 'update_ordering', orderingData);
					if (orderingBtn.hasClass('do-refresh')) {
						refreshDetails();
					}
				}
			});

			$(document).find('.droppable-lexeme').droppable({
				accept: function(draggableDiv) {
					if (draggableDiv.hasClass("draggable-synonym")) {
						let wordId = draggableDiv.data('word-id');
						let existingWord = $(this).find("input.meaning-word-id[value='" + wordId + "']");

						if (!existingWord.length) {
							return true;
						}
					}
				},
				classes: {
					"ui-droppable-active": "ui-state-active",
					"ui-droppable-hover": "ui-state-hover"
				},
				drop: function(event, ui) {

					let relationId = ui.draggable.parent().data('id');

					let meaningId = $(this).data('meaning-id');
					let lexemeId = $(this).data('lexeme-id');
					let wordId = ui.draggable.data('word-id');

					let actionUrl = applicationUrl + 'syn_create_lexeme/' + meaningId + '/' + wordId + '/' + lexemeId + '/' + relationId;

					openWaitDlg();
					let callbackFunc = () => refreshDetails();
					doPostRelationChange(actionUrl, callbackFunc);
				}
			});

			if (IS_KEYBOARD_MODE) {
				activateList(3);

			}

			//KEEP TRACK OF WHAT WAS THE LAST SEARCH RESULT DISPLAYED
			if (activeSearchResultID !== id) {
				activeSearchResultID = id;
			}
			//IF AN ALLREADY ACTIVE DETAILS VIEW WAS SELECTED KEEP THE SCROLLPOSITIONS
			else if (activeSearchResultID === id) {
				setScrollPositions(savedScrollPositions);
			}


		}).fail(function(data) {
			console.log(data);
			alert('Detailide päring ebaõnnestus, proovige hiljem uuesti.');
		});
	});

	$(document).on("click", ".rel-status-btn", function() {
		let status = $(this).data('status');
		let id = $(this).data('id');
		let actionUrl = applicationUrl + 'syn_relation_status?id=' + id + '&status=' + status;

		let callbackFunc = () => refreshDetails();

		doPostRelationChange(actionUrl, callbackFunc);

	});

	$(document).on('click', '.order-up', function() {
		let orderingBtn = $(this);
		let orderingData = changeItemOrdering(orderingBtn, -1);
		postJson(applicationUrl + 'update_ordering', orderingData);
		if (orderingBtn.hasClass('do-refresh')) {
			refreshDetails();
		}
	});

	$(document).on('click', '.order-down', function() {
		let orderingBtn = $(this);
		let orderingData = changeItemOrdering(orderingBtn, 1);
		postJson(applicationUrl + 'update_ordering', orderingData);
		if (orderingBtn.hasClass('do-refresh')) {
			refreshDetails();
		}
	});

	$(document).on("show.bs.modal", "[id^=addSynRelationDlg_]", function() {
		initAddSynRelationDlg($(this));
	});

	$(document).on("show.bs.modal", "[id^=editSynLexemeWeightDlg_]", function() {
		initGenericTextEditDlg($(this));
	});

	$(document).find('.draggable-synonym').draggable();
	$(document).find('.draggable-meaning').draggable();

	function doPostRelationChange(actionUrl, callbackFunc) {

		$.post(actionUrl).done(function(data) {
			if (data != '{}') {
				openAlertDlg("Andmete muutmine ebaõnnestus.");
				console.log(data);
			}
			callbackFunc();
		}).fail(function(data) {
			openAlertDlg("Andmete muutmine ebaõnnestus.");
			console.log(data);
		});
	}

	function isDisabledItem(activeDiv, navigateItem) {
		let panelIndex = activeDiv.attr('data-panel-index');

		if (panelIndex == "2") {
			let wordId = activeDiv.data('marked-word-id');

			return navigateItem.find('input.meaning-word-id[value="' + wordId + '"]').length != 0;
		}
		return false;
	}

	function unActivateItem(selectedItem, unSelect) {

		if (selectedItem != undefined) {
			selectedItem.removeClass(NAVIGATE_SELECTED_CLASS);
			selectedItem.removeClass(NAVIGATE_DECLINED_CLASS);

			if (unSelect) {
				selectedItem.removeAttr(NAVIGATE_SELECTED_ATTR);
			}
		}
	}

	function findSelectedNavigateItem(activeDiv) {
		let selectedItem = activeDiv.find('[' + NAVIGATE_SELECTED_ATTR + ']');

		if (selectedItem.length == 0) {
			selectedItem = activeDiv.find('[data-navigate-index="0"]');
		}

		return selectedItem;
	}

	function isValidPanelChangeKeyPress(keyCode) {
		let synDetailsVisible = $("#syn_details_div").html() != '';

		if (!synDetailsVisible) {
			return false;
		}

		let currentActiveList = $('div[data-active-panel]');
		let currentActivePanelIndex = currentActiveList.data('panel-index');

		if (keyCode == 37 || keyCode == 39) {

			if (currentActivePanelIndex != undefined) {
				let currentIndex = parseInt(currentActivePanelIndex);
				return ((currentIndex > 1 && keyCode == 37) || (currentIndex < 3 && keyCode == 39));
			}
		}

		return true;
	}

	function checkKey(e) {
		if (IS_KEYBOARD_MODE == false) {
			console.log("KEYBOARD MODE NOT ENABLED");
			return;
		}

		//TODO refactor all this

		var tag = e.target.tagName.toLowerCase();
		if (tag == 'input' || tag == 'textarea') {
			return;
		}

		if (($(".modal").data('bs.modal') || {})._isShown) {
			return;
		}

		let currentActiveList = $('div[data-active-panel]');
		let currentActivePanelIndex = currentActiveList.data('panel-index');
		let currentSelectedItem = currentActiveList.find('[data-navigate-selected]');
		let currentSelectedIndex = parseInt(currentSelectedItem.attr('data-navigate-index'));

		e = e || window.event;
		console.log(e.keyCode);

		if (e.keyCode == 38 || e.keyCode == 40) { // arrows up down

			if (currentSelectedItem.length != 0) {
				console.log('currentSelectedITem exists');
				let indexIncrement = (e.keyCode == 40 ? 1 : -1);
				let newIndex = currentSelectedIndex + indexIncrement;
				let newItem = currentActiveList.find('[data-navigate-index="' + newIndex + '"]');


				if (newItem.length != 0) {
					console.log('navItem exists');
					if (currentActivePanelIndex == "3") {
						changeSynonymDefinitionDisplay('hide');
					}
					newItem.addClass(isDisabledItem(currentActiveList, newItem) ? NAVIGATE_DECLINED_CLASS : NAVIGATE_SELECTED_CLASS);
					newItem.attr(NAVIGATE_SELECTED_ATTR, true);
					unActivateItem(currentSelectedItem, true);
					$(currentActiveList).stop(true);
					$(currentActiveList).scrollTo(newItem, 320, {axis: 'y', offset: -64});
					if (currentActivePanelIndex == "3") {
						changeSynonymDefinitionDisplay('show');
					}
				}
			}
		}

		// 1 - 3, arrows left-right
		if ((e.keyCode >= 49 && e.keyCode <= 51) || e.keyCode == 37 || e.keyCode == 39) {
			if (isValidPanelChangeKeyPress(e.keyCode)) {

				$('div[data-panel-index]').each(function() {
					$(this).removeAttr('data-active-panel').removeClass('keyboard-nav-list-active');
				});
				if (currentActivePanelIndex == "3") {
					changeSynonymDefinitionDisplay('hide');
				}

				let selectedPanelIndex = 1;
				let PANEL_KEYCODES = {"49": "1", "50": "2", "51": "3"};

				let isArrowKey = e.keyCode == 37 || e.keyCode == 39;
				if (isArrowKey) {
					if (currentActivePanelIndex != undefined) {
						selectedPanelIndex = parseInt(currentActivePanelIndex);
						if (e.keyCode == 37 && selectedPanelIndex > 1) {
							selectedPanelIndex--;
						} else if (e.keyCode == 39 && selectedPanelIndex < 3) {
							selectedPanelIndex++;
						}
					}
				} else {
					selectedPanelIndex = PANEL_KEYCODES[e.keyCode];
				}


				unActivateItem(currentSelectedItem, false);

				let activatedList = $('div[data-panel-index="' + selectedPanelIndex + '"]');
				activatedList.attr('data-active-panel', true).addClass('keyboard-nav-list-active');

				let selectedItem = findSelectedNavigateItem(activatedList);
				let isDisabled = isDisabledItem(activatedList, selectedItem);

				selectedItem.addClass(isDisabled ? NAVIGATE_DECLINED_CLASS : NAVIGATE_SELECTED_CLASS);
				selectedItem.attr('data-navigate-selected', true);
				if (selectedPanelIndex == "3") {
					changeSynonymDefinitionDisplay('show');
				}
			}

		}

		//Esc key
		if (e.keyCode == 27) {
			if (currentActivePanelIndex == "3") {
				changeSynonymDefinitionDisplay('hide');
			}
			$('.keyboard-nav-list').each(function() {
				$(this).removeAttr('data-marked-word-id');
				$(this).removeAttr('data-marked-relation-id'); //TODO refactor
				$(this).removeAttr('data-active-panel');
				$(this).find('[data-navigate-index]').each(function() {
					unActivateItem($(this), true);
				});
				$('.keyboard-nav-list-item-selected').removeClass('keyboard-nav-list-item-selected');

				$(this).find('.keyboard-nav-list-item-active').removeClass('keyboard-nav-list-item-active');

				$("#keyboardEditBtn").removeAttr('disabled');

				$(document).find('input[name="simpleSearchFilter"]').val('').focus();
			});
			$('body').removeClass('keyboard-edit-mode-active');
			IS_KEYBOARD_MODE = false;
		}

		//Enter key
		if (e.keyCode == 13) {
			e.preventDefault();
			//IF SYNONYM LIST IS ACTIVE AND USER PRESSES ENTER
			if (currentActivePanelIndex == "3") {
				changeSynonymDefinitionDisplay('hide');
				currentActiveList.removeClass('keyboard-nav-list-active').removeAttr('data-active-panel').find('.keyboard-nav-list-item-selected').each(function() {
					$(this).removeClass('.keyboard-nav-list-item-selected');
				});
				currentSelectedItem.addClass('keyboard-nav-list-item-selected');

				unActivateItem(currentSelectedItem, false);
				//TODO refactor
				let wordId = currentSelectedItem.children(':first').attr('data-word-id');
				let relationId = currentSelectedItem.attr('data-id');

				let activatedList = $('div[data-panel-index="' + 2 + '"]');
				activatedList.attr('data-active-panel', true).addClass('keyboard-nav-list-active');

				activatedList.data('marked-word-id', wordId);
				activatedList.data('marked-relation-id', relationId);

				let selectedLexemeItem = findSelectedNavigateItem(activatedList);

				let lexemeExists = selectedLexemeItem.find('input.meaning-word-id[value="' + wordId + '"]').length != 0;

				selectedLexemeItem.addClass(lexemeExists ? NAVIGATE_DECLINED_CLASS : NAVIGATE_SELECTED_CLASS);
				selectedLexemeItem.attr(NAVIGATE_SELECTED_ATTR, true);


			} else if (currentActivePanelIndex == "2") {
				if (!currentSelectedItem.hasClass(NAVIGATE_DECLINED_CLASS)) {
					let wordId = currentActiveList.data('marked-word-id'); //TODO move to a hidden field ? - add a marked attribute to the marked element
					let relationId = currentActiveList.data('marked-relation-id'); //TODO Refactor

					if (wordId != undefined) {

						let lexemeId = currentSelectedItem.data('lexeme-id');
						let meaningId = currentSelectedItem.data('meaning-id');

						let actionUrl = applicationUrl + 'syn_create_lexeme/' + meaningId + '/' + wordId + '/' + lexemeId + '/' + relationId;
						let callbackFunc = () => refreshDetails();
						doPostRelationChange(actionUrl, callbackFunc);

					} else {
						openAlertDlg("Ilmiku tekitamiseks vali paremalt tulbast sõna vajutades 'ENTER'.");
					}

				} else {
					openAlertDlg("Ilmik on juba olemas.");
				}
			} else if (currentActivePanelIndex == "1") {
				$(document).find('.keyboard-nav-list-item-selected').removeClass('keyboard-nav-list-item-selected');
				currentSelectedItem.find('button[name="synDetailsBtn"]').trigger('click');
			}
		}


	}

	$(document).on('keydown', checkKey);

	if ($('#synSearchResultsDiv').html() == undefined) {
		$(document).find('input[name="simpleSearchFilter"]').focus();
	}

	let detailButtons = $('#results').find('[name="synDetailsBtn"]');
	if (detailButtons.length === 1) {
		detailButtons.trigger('click');
	}

	$(document).on('show.bs.modal', '#processLogDlg', function(e) {
		var dlg = $(this);
		var link = $(e.relatedTarget);
		var url = link.attr('href');
		dlg.find('.close').focus();
		dlg.find('.modal-body').html(null);
		$.get(url).done(function(data) {
			dlg.find('.modal-body').html(data);
		});
	});

	$(document).on('show.bs.modal', '#wordLifecycleLogDlg', function(e) {
		let dlg = $(this);
		let link = $(e.relatedTarget);
		let url = link.attr('href');
		dlg.find('.close').focus();
		dlg.find('.modal-body').html(null);
		$.get(url).done(function(data) {
			dlg.find('.modal-body').html(data);
		});
	});

}

function getScrollPositions() {
	let scrollPositions = [];

	$('.keyboard-nav-list').each(function() {
		var scrollTop = $(this).scrollTop();
		scrollPositions.push(scrollTop);

	})
	return scrollPositions;
}

function setScrollPositions(positions) {
	$('.keyboard-nav-list').each(function(i) {
		$(this).scrollTop(positions[i]);
	})
}

function changeSynonymDefinitionDisplay(displayOption = 'toggle') {
	$('.keyboard-nav-list-item-active .list-item-value').tooltip(displayOption);
}

function refreshDetails() {
	let selectedWordId = $('#syn_details_div').data('id');
	var refreshButton = $('[name="synDetailsBtn"][data-id="' + selectedWordId + '"]');

	refreshButton.trigger('click');
	refreshButton.parent().addClass('active');
}
