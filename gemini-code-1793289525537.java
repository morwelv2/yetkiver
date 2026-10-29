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
        // Komutu kaydediyoruz
        if (getCommand("yetkiver") != null) {
            getCommand("yetkiver").setExecutor(this);
        }
        getLogger().info("YetkiVer plugini aktif!");
    }

    @Override
    public void onDisable() {
        getLogger().info("YetkiVer plugini deaktif oldu.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Sadece oyuncular kullanabilsin (Konsol karışmasın)
        if (!(sender instanceof Player)) {
            sender.sendMessage("Bu komut sadece oyun içinden kullanılabilir.");
            return true;
        }

        Player yonetici = (Player) sender;

        // Yetki kontrolü: Sadece OP olanlar kullanabilir
        if (!yonetici.isOp()) {
            yonetici.sendMessage(Component.text("Bu komutu kullanmak için yetkin yok!").color(NamedTextColor.RED));
            return true;
        }

        // Kullanım kontrolü: /yetkiver <oyuncu> <yetki>
        if (args.length < 2) {
            yonetici.sendMessage(Component.text("Kullanım: /yetkiver <oyuncu> <yetki>").color(NamedTextColor.YELLOW));
            return true;
        }

        String hedefIsim = args[0];
        String yetki = args[1];
        Player hedefOyuncu = Bukkit.getPlayer(hedefIsim);

        if (hedefOyuncu == null) {
            yonetici.sendMessage(Component.text("Belirtilen oyuncu oyunda bulunamadı!").color(NamedTextColor.RED));
            return true;
        }

        // Mesajı oluşturuyoruz: "X adlı yönetici, x kişisine x yetkisini verdi."
        Component logMesaji = Component.text()
                .append(Component.text(yonetici.getName()).color(NamedTextColor.RED))
                .append(Component.text(" adlı yönetici, ").color(NamedTextColor.RED))
                .append(Component.text(hedefOyuncu.getName()).color(NamedTextColor.RED))
                .append(Component.text(" kişisine ").color(NamedTextColor.RED))
                .append(Component.text(yetki).color(NamedTextColor.RED))
                .append(Component.text(" yetkisini verdi.").color(NamedTextColor.RED))
                .build();

        // Sadece OP olan oyunculara ve konsola bu logu gönderelim
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(logMesaji);
            }
        }
        // Konsola da düşmesini istersen:
        Bukkit.getConsoleSender().sendMessage(logMesaji);

        // İsteğe bağlı: Gerçek yetkiyi LuckPerms veya sunucu yetki sistemine bağlamak istersen 
        // buraya Vault/LuckPerms komut tetikleyicisi ekleyebiliriz ama şimdilik sadece log/chat bildirimi yapıyor.
        yonetici.sendMessage(Component.text("Yetki verme işlemi kaydedildi ve OP'lere bildirildi.").color(NamedTextColor.GREEN));

        return true;
    }
}