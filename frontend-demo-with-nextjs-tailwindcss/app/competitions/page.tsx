export default function Home() {
    return (
        <main>
            <h1>Wettkämpfe</h1>
            <table className="table-auto w-full">
                <thead>
                <tr>
                    <th>Datum</th>
                    <th>Name</th>
                    <th>Kürzel</th>
                    <th>Ort</th>
                </tr>
                </thead>
                <tbody>
                <tr className="table-odd">
                    <td>12.-14.04.2024</td>
                    <td><a href="competitions/dem2024">5. Int. Deutsche Einzelstrecken-Meisterschaften</a></td>
                    <td>DEM</td>
                    <td>Düsseldorf</td>
                </tr>
                <tr className="table-even">
                    <td>03.-05.05.2024</td>
                    <td>Spanische Poolmeisterschaften</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr className="table-odd">
                    <td>04.05.2024</td>
                    <td>Walldürner SERC-Pokal</td>
                    <td></td>
                    <td>Walldürn (Baden)</td>
                </tr>
                <tr className="table-even">
                    <td>10.-12..05.2024</td>
                    <td>32. Deutsche Seniorenmeisterschaften</td>
                    <td>DSM</td>
                    <td>Rheda-Wiedenbrück</td>
                </tr>
                <tr className="table-odd">
                    <td>15.06.2024</td>
                    <td>DLRG Trophy 1</td>
                    <td></td>
                    <td>Silbersee (Haltern am See)</td>
                </tr>
                <tr className="table-even">
                    <td>18..20.07.2024</td>
                    <td>DLRG Cup</td>
                    <td>DCO</td>
                    <td>Warnemünde</td>
                </tr>
                </tbody>
            </table>
        </main>
    );
}
