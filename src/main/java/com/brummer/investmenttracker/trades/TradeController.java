package com.brummer.investmenttracker.trades;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tradeController")
public class TradeController {

	private final TradeRepository tradeRepositiry;
	private final String redirect = "redirect:/tradeController/trades";
	
	public TradeController(TradeRepository tradeRepository) {
		this.tradeRepositiry = tradeRepository;
	}
	
	@RequestMapping("/trades")
	public String trades(Model model) {
		model.addAttribute("trades", tradeRepositiry.findAll());
		return "trades";
	}
	
	@PostMapping("/addUpdateTrade")
	public String addTrade(@ModelAttribute Trade trade) {
		tradeRepositiry.save(trade);
		return redirect;
	}
	
	@GetMapping("/deleteTrade")
	public String deleteTrade(@RequestParam String id) {
		if(id != null) {
			Long idLong = Long.parseLong(id);
			tradeRepositiry.deleteById(idLong);
		}
		return redirect;
	}
	
	@GetMapping("/editTrade")
	public String editTrade(@RequestParam String id, Model model) {
		if(id != null) {
			Long idLong = Long.valueOf(id);
			model.addAttribute("trade", tradeRepositiry.findById(idLong).get());
			model.addAttribute("trades", tradeRepositiry.findAll());
		}
		return "trades";
	}
	
}
