package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

@Controller
@RequestMapping("/todo")
class TodoController{

	private final Set<Todo> todos = new ConcurrentSkipListSet<>(Comparator.comparingInt(Todo::id));

	TodoController() {
		for (var t : "test code,my dream,vacation story".split(",")) {
			todos.add(Todos.todo(t));
		}
	}

	@GetMapping
	String todo(Model model){
		model.addAttribute("todos", this.todos);
		return "todo";
	}

	@DeleteMapping("/{todoId}")
	@ResponseBody
	String delete(@PathVariable String todoId){
		this.todos.stream()
				.filter(todo -> todo.id().equals(todoId))
				.forEach(this.todos::remove);
		return "";
	}
}

record Todo(Integer id, String title){}

class Todos{
	private static final AtomicInteger id = new AtomicInteger(0);
	static Todo todo(String title){
		return new Todo( id.incrementAndGet(), title);
	}
}