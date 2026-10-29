package com.yetkiver;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class YetkiVer extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        if (getCommand("yetkiver") != null) {
            getCommand("yetkiver").setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Bu komut sadece oyun icinden kullanilabilir.");
            return true;
        }

        Player yonetici = (Player) sender;

        if (!yonetici.isOp()) {
            yonetici.sendMessage(Component.text("Bu komutu kullanmak icin yetkin yok!").color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 2) {
            yonetici.sendMessage(Component.text("Kullanim: /yetkiver <oyuncu> <yetki>").color(NamedTextColor.YELLOW));
            return true;
        }

        String hedefIsim = args[0];
        String yetki = args[1];
        Player hedefOyuncu = Bukkit.getPlayer(hedefIsim);

        if (hedefOyuncu == null) {
            yonetici.sendMessage(Component.text("Belirtilen oyuncu oyunda bulunamadi!").color(NamedTextColor.RED));
            return true;
        }

        Component logMesaji = Component.text()
                .append(Component.text(yonetici.getName()).color(NamedTextColor.RED))
                .append(Component.text(" adli yonetici, ").color(NamedTextColor.RED))
                .append(Component.text(hedefOyuncu.getName()).color(NamedTextColor.RED))
                .append(Component.text(" kisisine ").color(NamedTextColor.RED))
                .append(Component.text(yetki).color(NamedTextColor.RED))
                .append(Component.text(" yetkisini verdi.").color(NamedTextColor.RED))
                .build();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(logMesaji);
            }
        }
        Bukkit.getConsoleSender().sendMessage(logMesaji);

        yonetici.sendMessage(Component.text("Yetki verme islemi kaydedildi ve OP'lere bildirildi.").color(NamedTextColor.GREEN));

        return true;
    }
}
