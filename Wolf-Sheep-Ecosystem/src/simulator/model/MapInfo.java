package simulator.model;

public interface MapInfo extends JSONable, Iterable<MapInfo.RegionData> { 
	
	public record RegionData(int row, int col, RegionInfo r) implements Comparable<RegionData> {

		@Override
		public int compareTo(RegionData o) {
			if(this.row > o.row) {
				return 1;
			}else if(this.row == o.row) {
				if (this.col > o.col) {
					return 1;
				}else if (this.col == o.col){
					return 0;
				}else {
					return -1;
				}
			}else {
				return -1;
			}
		}
		
	}
	
	public int get_cols(); 
	public int get_rows(); 
	public int get_width(); 
	public int get_height(); 
	public int get_region_width(); 
	public int get_region_height(); 
	
}