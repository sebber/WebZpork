$ ->
	$(".tabbable").tab 'profile'

	$('.delete-btn').click (event) =>
		unless confirm "Are you really sure!?"
			event.preventDefault()

	$('.actions a').tooltip()